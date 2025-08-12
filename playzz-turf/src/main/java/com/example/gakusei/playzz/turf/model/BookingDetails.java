package com.example.gakusei.playzz.turf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "BOOKING_DETAILS")
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_details_seq_gen")
    @SequenceGenerator(name = "booking_details_seq_gen", sequenceName = "booking_details_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private Integer slotId;
    @NotNull
    private LocalDate slotDate;

    @NotNull
    private LocalTime slotStartTime;

    @NotNull
    private LocalTime slotEndTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private LocalDateTime canceledAt;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDateTime createdAt;

    public BookingDetails() {
    }

    public BookingDetails(Long id, Integer slotId, LocalDate slotDate, LocalTime slotStartTime, LocalTime slotEndTime, BookingStatus bookingStatus, LocalDateTime canceledAt, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.slotId = slotId;
        this.slotDate = slotDate;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.bookingStatus = bookingStatus;
        this.canceledAt = canceledAt;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(LocalTime slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public LocalTime getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(LocalTime slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

