package com.example.gakusei.playzz.turf.service;

import com.example.gakusei.playzz.turf.dto.TurfSlotDto;
import com.example.gakusei.playzz.turf.model.TurfSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurfSlotService {
    void generateSlotsForDate(LocalDate date);
    void initializeSlots();
    void maintainRollingWindow();
    List<TurfSlot> viewAllSlots();
    String bookSlot(LocalDate date, LocalTime startTime, LocalTime endTime, Long userId);
    String cancelSlot(LocalDate date, LocalTime startTime, LocalTime endTime, Long userId);
    List<TurfSlot> getAvailableSlots();
    List<TurfSlotDto> getAvailableSlotsForUser(Long userId);
}