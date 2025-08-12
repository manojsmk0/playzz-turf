package com.example.gakusei.playzz.turf.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "TURF_SLOT")
public class TurfSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turf_slot_seq_gen")
    @SequenceGenerator(name = "turf_slot_seq_gen", sequenceName = "turf_slot_seq", allocationSize = 1)
    private Long id;
    @NotNull
    private Integer slotId;
    @NotNull
    private Integer totalSlots =10;
    @NotNull
    private Integer availableSlots = 10;
    @NotNull
    private LocalTime slotStartTime;
    @NotNull
    private LocalTime slotEndTime;
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

    public TurfSlot(Long id, Integer slotId, Integer totalSlots, Integer availableSlots, LocalTime slotStartTime, LocalTime slotEndTime, Long durationInMinutes, LocalDate slotDate, SlotStatus slotStatus, LocalDateTime statusUpdatedTime) {
        this.id = id;
        this.slotId = slotId;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
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

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
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