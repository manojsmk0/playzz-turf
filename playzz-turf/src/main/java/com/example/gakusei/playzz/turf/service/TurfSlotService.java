package com.example.gakusei.playzz.turf.service;

import com.example.gakusei.playzz.turf.model.TurfSlot;

import java.time.LocalDate;
import java.util.List;

public interface TurfSlotService {
    void generateSlotsForDate(LocalDate date);
    void initializeSlots();
    void maintainRollingWindow();
    List<TurfSlot> getAvailableSlots();
    List<TurfSlot> viewAllSlots();
    String bookSlot(Long slotId, Long userId);  // Added userId parameter
    String cancelSlot(Long slotId, Long userId); // Added userId parameter
}