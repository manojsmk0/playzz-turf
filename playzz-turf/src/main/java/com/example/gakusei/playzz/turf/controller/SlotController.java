package com.example.gakusei.playzz.turf.controller;

import com.example.gakusei.playzz.turf.model.TurfSlot;
import com.example.gakusei.playzz.turf.repository.TurfSlotRepository;
import com.example.gakusei.playzz.turf.service.TurfSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playzzturf")
public class SlotController {
    private final TurfSlotService turfSlotService;
    private  final TurfSlotRepository turfSlotRepository;

    @Autowired
    public SlotController(TurfSlotService turfSlotService,TurfSlotRepository turfSlotRepository) {
        this.turfSlotService = turfSlotService;
        this.turfSlotRepository =turfSlotRepository;
    }

    @GetMapping("/availableslots")
    public ResponseEntity<List<TurfSlot>> availableSlots() {
        List<TurfSlot> availableSlots = turfSlotService.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/bookslot")
    public ResponseEntity<String> bookSlot(
            @RequestParam Long slotId,
            @RequestParam Long userId) {

        String result = turfSlotService.bookSlot(slotId, userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cancelslot")
    public ResponseEntity<String> cancelSlot(
            @RequestParam Long bookingId,  // Changed from slotId to bookingId
            @RequestParam Long userId) {

        String result = turfSlotService.cancelSlot(bookingId, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allslots")
    public ResponseEntity<List<TurfSlot>> allSlot() {
        List<TurfSlot> allSlots = turfSlotService.viewAllSlots();
        return ResponseEntity.ok(allSlots);
    }

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeSlot() {
        if (turfSlotRepository.count() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Slots already initialized");
        }
        turfSlotService.initializeSlots();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Slots initialized for 4 days");
    }

    @PostMapping("/maintain")
    public ResponseEntity<String> runRollingWindow() {
        turfSlotService.maintainRollingWindow();
        return ResponseEntity.ok("Slot window maintained successfully");
    }
}