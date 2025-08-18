package com.example.gakusei.playzz.turf.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurfSlotDto {
    private Long id;
    private Integer slotId;
    private LocalDate slotDate;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private int availableSlots;
    private String slotStatus;
    private boolean bookedByUser;

    public TurfSlotDto() {
    }

    public TurfSlotDto(Long id, Integer slotId, LocalDate slotDate, LocalTime slotStartTime, LocalTime slotEndTime, int availableSlots, String slotStatus, boolean bookedByUser) {
        this.id = id;
        this.slotId = slotId;
        this.slotDate = slotDate;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.availableSlots = availableSlots;
        this.slotStatus = slotStatus;
        this.bookedByUser = bookedByUser;
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

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public boolean isBookedByUser() {
        return bookedByUser;
    }

    public void setBookedByUser(boolean bookedByUser) {
        this.bookedByUser = bookedByUser;
    }
}
