package com.example.gakusei.playzz.turf.implementation;

import com.example.gakusei.playzz.turf.model.*;
import com.example.gakusei.playzz.turf.repository.BookingDetailsRepo;
import com.example.gakusei.playzz.turf.repository.TurfSlotRepository;
import com.example.gakusei.playzz.turf.repository.UserRepo;
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

    @Autowired
    public TurfSlotServiceImpl(TurfSlotRepository turfSlotRepo,UserRepo userRepo,BookingDetailsRepo bookingDetailsRepo) {
        this.turfSlotRepo = turfSlotRepo;
        this.userRepo = userRepo;
        this.bookingDetailsRepo =bookingDetailsRepo;
    }

    @Transactional
    public void generateSlotsForDate(LocalDate date) {
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        long durationInMinutes = 120;  // Use long instead of int

        // Create datetime boundaries
        LocalDateTime slotStart = LocalDateTime.of(date, startTime);
        LocalDateTime slotEndBoundary = LocalDateTime.of(date, endTime);

        while (slotStart.isBefore(slotEndBoundary)) {
            LocalDateTime nextSlot = slotStart.plusMinutes(durationInMinutes);

            // Stop if next slot would exceed boundary
            if (nextSlot.isAfter(slotEndBoundary)) {
                break;
            }

            // Check if slot exists
            if (turfSlotRepo.existsBySlotDateAndStartTime(date, slotStart)) {
                logger.info("Slot at {} already exists, skipping", slotStart);
                slotStart = nextSlot;
                continue;
            }

            // Create new slot
            TurfSlot slot = new TurfSlot();
            slot.setTotalSlots(10);
            slot.setAvailableSlots(10);
            slot.setSlotDate(date);
            slot.setStartTime(slotStart);
            slot.setEndTime(nextSlot);
            slot.setDurationInMinutes(durationInMinutes);
            slot.setSlotStatus(SlotStatus.OPEN);
            slot.setStatusUpdatedTime(LocalDateTime.now());

            try {
                turfSlotRepo.save(slot);
                logger.info("Created slot: {} to {}", slotStart, nextSlot);
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to create slot: {}", e.getMessage());
            }

            slotStart = nextSlot;
        }
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
        LocalDate purgeDate = today.minusDays(1);

        // Delete slots older than yesterday
        int deleted = turfSlotRepo.deleteBySlotDateBefore(purgeDate);
        logger.info("Deleted {} old slots", deleted);

        // Create slots for new window
        LocalDate lastDate = today.plusDays(ROLLING_WINDOW_DAYS - 1);
        for (LocalDate date = today; !date.isAfter(lastDate); date = date.plusDays(1)) {
            if (!turfSlotRepo.existsBySlotDate(date)) {
                generateSlotsForDate(date);
            }
        }
    }

    public List<TurfSlot> getAvailableSlots() {
        LocalDateTime now = LocalDateTime.now();
        return turfSlotRepo.findAvailableSlots(now);
    }

    @Transactional
    public String bookSlot(Long slotId, Long userId) {
        Optional<TurfSlot> optional = turfSlotRepo.findById(slotId);
        if (optional.isEmpty()) return "Slot not found";

        TurfSlot slot = optional.get();

        if (slot.getEndTime().isBefore(LocalDateTime.now())) {
            return "Cannot book expired slot";
        }

        switch (slot.getSlotStatus()) {
            case BOOKED:
                return "Slot is already booked";

            case OPEN:
                if (slot.getAvailableSlots() <= 0) {
                    return "No available slots";
                }

                slot.setAvailableSlots(slot.getAvailableSlots() - 1);
                if (slot.getAvailableSlots() == 0) {
                    slot.setSlotStatus(SlotStatus.BOOKED);
                }
                slot.setStatusUpdatedTime(LocalDateTime.now());
                turfSlotRepo.save(slot);

                // In real implementation: Create Booking record here
                Users user = userRepo.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                BookingDetails booking = new BookingDetails();

                booking.setSlotId(slotId);
                booking.setUserId(userId);
                booking.setSlotTime(LocalDateTime.now());
                booking.setCreatedAt(LocalDateTime.now());
                booking.setBookingStatus(BookingStatus.CONFIRMED);

                bookingDetailsRepo.save(booking);

                return "Slot booked successfully";

            default:
                return "Slot is not available for booking";
        }
    }

    @Transactional
    public String cancelSlot(Long slotId, Long userId) {
        Optional<TurfSlot> optional = turfSlotRepo.findById(slotId);
        if (optional.isEmpty()) return "Slot not found";

        TurfSlot slot = optional.get();
        LocalDateTime now = LocalDateTime.now();

        if (slot.getSlotStatus() != SlotStatus.BOOKED && slot.getAvailableSlots() >= slot.getTotalSlots()) {
            return "Slot is not booked";
        }

        if (now.isAfter(slot.getStartTime().minusHours(CANCELLATION_DEADLINE_HOURS))) {
            return "Cancellation must be done " + CANCELLATION_DEADLINE_HOURS + " hours before start time";
        }

        slot.setAvailableSlots(slot.getAvailableSlots() + 1);
        if (slot.getSlotStatus() == SlotStatus.BOOKED) {
            slot.setSlotStatus(SlotStatus.OPEN);
        }
        slot.setStatusUpdatedTime(now);
        turfSlotRepo.save(slot);

        // In real implementation: Update Booking record here
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BookingDetails booking = new BookingDetails();

        booking.setSlotId(slotId);
        booking.setUserId(userId);
        booking.setSlotTime(LocalDateTime.now());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.CANCELLED);

        bookingDetailsRepo.save(booking);

        return "Booking cancelled successfully";
    }

    public List<TurfSlot> viewAllSlots() {
        return turfSlotRepo.findAll();
    }
}