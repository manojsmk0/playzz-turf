package com.example.gakusei.playzz.turf.controller;

import com.example.gakusei.playzz.turf.dto.TurfSlotDto;
import com.example.gakusei.playzz.turf.model.TurfSlot;
import com.example.gakusei.playzz.turf.repository.TurfSlotRepository;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import com.example.gakusei.playzz.turf.service.TurfSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/playzzturf")
public class SlotController {

    private final TurfSlotService turfSlotService;
    private final TurfSlotRepository turfSlotRepository;
    private final UserRepo userRepo;

    @Autowired
    public SlotController(TurfSlotService turfSlotService, TurfSlotRepository turfSlotRepository, UserRepo userRepo) {
        this.turfSlotService = turfSlotService;
        this.turfSlotRepository = turfSlotRepository;
        this.userRepo = userRepo;
    }

    // Get available slots for logged-in user
    @GetMapping("/availableslots")
    public ResponseEntity<List<TurfSlotDto>> availableSlots() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Long userId = userRepo.findByUserName(username).get().getId();

        List<TurfSlotDto> slots = turfSlotService.getAvailableSlotsForUser(userId);
        return ResponseEntity.ok(slots);
    }

    // Book a slot
    @PostMapping("/bookslot")
    public ResponseEntity<String> bookSlot(
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Long userId = userRepo.findByUserName(username).get().getId();

        String result = turfSlotService.bookSlot(date, startTime, endTime, userId);
        return ResponseEntity.ok(result);
    }

    // Cancel a slot
    @PutMapping("/cancelslot")
    public ResponseEntity<String> cancelSlot(
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Long userId = userRepo.findByUserName(username).get().getId();

        String result = turfSlotService.cancelSlot(date, startTime, endTime, userId);
        return ResponseEntity.ok(result);
    }

    // View all slots
    @GetMapping("/allslots")
    public ResponseEntity<List<TurfSlot>> allSlots() {
        List<TurfSlot> slots = turfSlotService.viewAllSlots();
        return ResponseEntity.ok(slots);
    }

    // Admin: initialize slots
    @PostMapping("/admin/initialize")
    public ResponseEntity<String> initializeSlots() {
        if (turfSlotRepository.count() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Slots already initialized");
        }
        turfSlotService.initializeSlots();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Slots initialized for 4 days");
    }

    // Admin: maintain rolling window
    @PostMapping("/admin/maintain")
    public ResponseEntity<String> maintainSlots() {
        turfSlotService.maintainRollingWindow();
        return ResponseEntity.ok("Slot window maintained successfully");
    }
}
