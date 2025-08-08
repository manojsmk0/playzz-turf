package com.example.gakusei.playzz.turf.implementation;

import com.example.gakusei.playzz.turf.service.TurfSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SlotScheduling {
    private static final Logger logger = LoggerFactory.getLogger(SlotScheduling.class);
    private final TurfSlotService turfSlotService;

    @Autowired
    public SlotScheduling(TurfSlotService turfSlotService) {
        this.turfSlotService = turfSlotService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void autoMaintainSlots() {
        try {
            turfSlotService.maintainRollingWindow();
            logger.info("Slot maintenance completed successfully");
        } catch (Exception e) {
            logger.error("Slot maintenance failed: {}", e.getMessage());
        }
    }
}