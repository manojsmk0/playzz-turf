package com.example.gakusei.playzz.turf.mail;

import com.example.gakusei.playzz.turf.model.BookingDetails;
import com.example.gakusei.playzz.turf.model.BookingStatus;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.BookingDetailsRepo;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import com.example.gakusei.playzz.turf.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleEmailTasks {

    private final BookingDetailsRepo bookingDetailsRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;

    @Autowired
    public ScheduleEmailTasks(BookingDetailsRepo bookingDetailsRepo, UserRepo userRepo, EmailService emailService) {
        this.bookingDetailsRepo = bookingDetailsRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000) // every 15 min
    public void sendSlotReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.plusMinutes(60);
        LocalDateTime windowEnd = now.plusMinutes(75);

        LocalDate startDate = windowStart.toLocalDate();
        LocalDate endDate = windowEnd.toLocalDate();

        List<BookingDetails> bookingsToRemind;

        if (startDate.equals(endDate)) {
            // Window within same date, query once
            bookingsToRemind = bookingDetailsRepo.findBySlotDateAndStartTimeBetweenAndStatus(
                    startDate,
                    windowStart.toLocalTime(),
                    windowEnd.toLocalTime(),
                    BookingStatus.CONFIRMED
            );
        } else {
            // Window crosses midnight, query for both dates and combine
            List<BookingDetails> firstPart = bookingDetailsRepo.findBySlotDateAndStartTimeBetweenAndStatus(
                    startDate,
                    windowStart.toLocalTime(),
                    LocalTime.MAX,
                    BookingStatus.CONFIRMED
            );
            List<BookingDetails> secondPart = bookingDetailsRepo.findBySlotDateAndStartTimeBetweenAndStatus(
                    endDate,
                    LocalTime.MIN,
                    windowEnd.toLocalTime(),
                    BookingStatus.CONFIRMED
            );
            bookingsToRemind = new ArrayList<>();
            bookingsToRemind.addAll(firstPart);
            bookingsToRemind.addAll(secondPart);
        }

        for (BookingDetails booking : bookingsToRemind) {
            Users user = userRepo.findById(booking.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            emailService.sendRemainder(
                    user.getEmail(),
                    user.getName(),
                    booking.getSlotDate(),
                    booking.getSlotStartTime()
            );
        }
    }

}


