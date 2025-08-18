package com.example.gakusei.playzz.turf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/playzzturf")
public class BookingController {

    @GetMapping("/booking")
    public String showBookingPage() {
        return "booking"; // maps to booking.html
    }
}
