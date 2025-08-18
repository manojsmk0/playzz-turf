package com.example.gakusei.playzz.turf.implementation;

import com.example.gakusei.playzz.turf.dto.TurfSlotDto;
import com.example.gakusei.playzz.turf.model.*;
import com.example.gakusei.playzz.turf.repository.BookingDetailsRepo;
import com.example.gakusei.playzz.turf.repository.TurfSlotRepository;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import com.example.gakusei.playzz.turf.service.EmailService;
import com.example.gakusei.playzz.turf.service.TurfSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TurfSlotServiceImpl implements TurfSlotService {

    private static final Logger logger = LoggerFactory.getLogger(TurfSlotServiceImpl.class);
    private static final int ROLLING_WINDOW_DAYS = 4;
    private static final long DEFAULT_SLOT_DURATION = 120; // minutes
    private static final int CANCELLATION_DEADLINE_HOURS = 48;

    private final TurfSlotRepository turfSlotRepo;
    private final UserRepo userRepo;
    private final BookingDetailsRepo bookingDetailsRepo;
    private final EmailService emailService;

    @Autowired
    public TurfSlotServiceImpl(TurfSlotRepository turfSlotRepo, UserRepo userRepo, BookingDetailsRepo bookingDetailsRepo,EmailService emailService) {
        this.turfSlotRepo = turfSlotRepo;
        this.userRepo = userRepo;
        this.bookingDetailsRepo = bookingDetailsRepo;
        this.emailService =emailService;
    }

    @Transactional
    public void generateSlotsForDate(LocalDate date) {
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        long durationInMinutes = DEFAULT_SLOT_DURATION;

        LocalTime slotStart = startTime;
        int slotIdCounter = 1;  // Start at 1

        while (slotStart.isBefore(endTime)) {
            LocalTime nextSlot = slotStart.plusMinutes(durationInMinutes);

            if (nextSlot.isAfter(endTime)) break;

            if (turfSlotRepo.existsBySlotDateAndSlotStartTime(date, slotStart)) {
                logger.info("Slot on {} at {} already exists, skipping", date, slotStart);
                slotStart = nextSlot;
                slotIdCounter++;  // still increment since time moves forward
                continue;
            }

            TurfSlot slot = new TurfSlot();
            slot.setSlotId(slotIdCounter);  // Assign 1 to 8 here
            slot.setTotalSlots(10);
            slot.setAvailableSlots(10);
            slot.setSlotDate(date);
            slot.setSlotStartTime(slotStart);
            slot.setSlotEndTime(nextSlot);
            slot.setDurationInMinutes(durationInMinutes);
            slot.setSlotStatus(SlotStatus.OPEN);
            slot.setStatusUpdatedTime(LocalDateTime.now());

            try {
                turfSlotRepo.save(slot);
                logger.info("Created slot: {} {} - {} with slotID {}", date, slotStart, nextSlot, slotIdCounter);
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to create slot: {}", e.getMessage());
            }

            slotStart = nextSlot;
            slotIdCounter++;  // increment counter for next slot
        }
    }

    public List<TurfSlotDto> getAvailableSlotsForUser(Long userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<TurfSlot> slots = turfSlotRepo.findAvailableSlots(today, now);

        return slots.stream().map(slot -> {
            TurfSlotDto dto = new TurfSlotDto();
            dto.setId(slot.getId());
            dto.setSlotId(slot.getSlotId());
            dto.setSlotDate(slot.getSlotDate());
            dto.setSlotStartTime(slot.getSlotStartTime());
            dto.setSlotEndTime(slot.getSlotEndTime());
            dto.setAvailableSlots(slot.getAvailableSlots());
            dto.setSlotStatus(slot.getSlotStatus().name());

            boolean bookedByUser = bookingDetailsRepo.existsBySlotIdAndUserIdAndBookingStatus(
                    slot.getId().intValue(), userId, BookingStatus.CONFIRMED
            );
            dto.setBookedByUser(bookedByUser);

            return dto;
        }).toList();
    }


    @Transactional
    public void initializeSlots() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < ROLLING_WINDOW_DAYS; i++) {
            generateSlotsForDate(today.plusDays(i));
        }
        logger.info("Initialized slots for {} days", ROLLING_WINDOW_DAYS);
    }

    @Transactional
    public void maintainRollingWindow() {
        LocalDate today = LocalDate.now();

        // Delete all slots strictly before today
        int deleted = turfSlotRepo.deleteBySlotDateBefore(today);
        logger.info("Deleted {} old slots", deleted);

        // Generate slots for the next 4 days starting today
        LocalDate lastDate = today.plusDays(ROLLING_WINDOW_DAYS - 1);
        for (LocalDate date = today; !date.isAfter(lastDate); date = date.plusDays(1)) {
            if (!turfSlotRepo.existsBySlotDate(date)) {
                generateSlotsForDate(date);
            }
        }
    }


    public List<TurfSlot> getAvailableSlots() {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return turfSlotRepo.findAvailableSlots(today, currentTime);
    }

    @Transactional
    public String bookSlot(LocalDate date, LocalTime startTime, LocalTime endTime, Long userId) {
        Optional<TurfSlot> optional = turfSlotRepo.findBySlotDateAndSlotStartTime(date, startTime);
        if (optional.isEmpty()) return "Slot not found";

        TurfSlot slot = optional.get();

        LocalDateTime slotStartDateTime = LocalDateTime.of(slot.getSlotDate(), slot.getSlotStartTime());
        LocalDateTime slotEndDateTime = LocalDateTime.of(slot.getSlotDate(), slot.getSlotEndTime());
        LocalDateTime now = LocalDateTime.now();

        if (slotEndDateTime.isBefore(now)) return "Cannot book expired slot";

        if (bookingDetailsRepo.existsBySlotIdAndUserIdAndBookingStatus(slot.getId().intValue(), userId, BookingStatus.CONFIRMED)) {
            return "You already have a booking for this slot";
        }

        if (slot.getAvailableSlots() <= 0) return "No available slots";

        slot.setAvailableSlots(slot.getAvailableSlots() - 1);
        if (slot.getAvailableSlots() == 0) slot.setSlotStatus(SlotStatus.BOOKED);
        slot.setStatusUpdatedTime(now);
        turfSlotRepo.save(slot);

        BookingDetails booking = new BookingDetails();
        booking.setSlotId(slot.getId().intValue());
        booking.setUserId(userId);
        booking.setSlotDate(slot.getSlotDate());
        booking.setSlotStartTime(slot.getSlotStartTime());
        booking.setSlotEndTime(slot.getSlotEndTime());
        booking.setCreatedAt(now);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingDetailsRepo.save(booking);

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailService.sendConfirmBookingEmail(
                user.getEmail(),
                user.getName(),
                slot.getSlotDate(),
                slot.getSlotStartTime(),
                slot.getSlotEndTime()
        );

        return "Slot booked successfully";

    }

    @Transactional
    public String cancelSlot(LocalDate date, LocalTime startTime, LocalTime endTime, Long userId) {
        Optional<TurfSlot> optional = turfSlotRepo.findBySlotDateAndSlotStartTime(date, startTime);
        if (optional.isEmpty()) return "Slot not found";

        TurfSlot slot = optional.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime slotStartDateTime = LocalDateTime.of(slot.getSlotDate(), slot.getSlotStartTime());

        if (now.isAfter(slotStartDateTime.minusHours(CANCELLATION_DEADLINE_HOURS))) {
            return "Cancellation must be done " + CANCELLATION_DEADLINE_HOURS + " hours before start time";
        }

        BookingDetails booking = bookingDetailsRepo
                .findBySlotIdAndUserIdAndBookingStatus(slot.getId().intValue(), userId, BookingStatus.CONFIRMED)
                .orElseThrow(() -> new RuntimeException("No active booking found to cancel"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCanceledAt(now);
        bookingDetailsRepo.save(booking);

        slot.setAvailableSlots(slot.getAvailableSlots() + 1);
        if (slot.getSlotStatus() == SlotStatus.BOOKED) {
            slot.setSlotStatus(SlotStatus.OPEN);
        }
        slot.setStatusUpdatedTime(now);
        turfSlotRepo.save(slot);

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailService.sendConfirmCancelEmail(
                user.getEmail(),
                user.getName(),
                slot.getSlotDate(),
                slot.getSlotStartTime(),
                slot.getSlotEndTime()
        );

        return "Booking cancelled successfully";

    }

    public List<TurfSlot> viewAllSlots() {
        return turfSlotRepo.findAll();
    }
}
