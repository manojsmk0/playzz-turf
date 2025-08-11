package com.example.gakusei.playzz.turf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKING_DETAILS")
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_details_seq_gen")
    @SequenceGenerator(name = "booking_details_seq_gen", sequenceName = "booking_details_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private Long slotId;

    @NotNull
    private LocalDateTime slotTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;  // Changed from SlotStatus to BookingStatus

    @NotNull
    private Long userId;  // Changed from userName to userId

    @NotNull
    private LocalDateTime createdAt;

    // Removed userEmail field

    public BookingDetails() {}

    // Updated constructor
    public BookingDetails(Long id, Long slotId, LocalDateTime slotTime,
                          BookingStatus bookingStatus, Long userId,
                          LocalDateTime createdAt) {
        this.id = id;
        this.slotId = slotId;
        this.slotTime = slotTime;
        this.bookingStatus = bookingStatus;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public LocalDateTime getSlotTime() { return slotTime; }
    public void setSlotTime(LocalDateTime slotTime) { this.slotTime = slotTime; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

