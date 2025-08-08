package com.example.gakusei.playzz.turf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TURF_SLOT")
public class TurfSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turf_slot_seq_gen")
    @SequenceGenerator(name = "turf_slot_seq_gen", sequenceName = "turf_slot_seq", allocationSize = 1)
    private Long id;
    @NotNull
    private Integer totalSlots =10;
    @NotNull
    private Integer availableSlots = 10;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private Long durationInMinutes;
    @NotNull
    @Column(name = "slot_date")
    private LocalDate slotDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;
    @NotNull
    private LocalDateTime statusUpdatedTime;

    public TurfSlot() {
    }

    public TurfSlot(Long id, Integer totalSlots, Integer availableSlots, LocalDateTime startTime, LocalDateTime endTime, Long durationInMinutes, LocalDate slotDate, SlotStatus slotStatus, LocalDateTime statusUpdatedTime) {
        this.id = id;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationInMinutes = durationInMinutes;
        this.slotDate = slotDate;
        this.slotStatus = slotStatus;
        this.statusUpdatedTime = statusUpdatedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public SlotStatus getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(SlotStatus slotStatus) {
        this.slotStatus = slotStatus;
    }

    public LocalDateTime getStatusUpdatedTime() {
        return statusUpdatedTime;
    }

    public void setStatusUpdatedTime(LocalDateTime statusUpdatedTime) {
        this.statusUpdatedTime = statusUpdatedTime;
    }
}