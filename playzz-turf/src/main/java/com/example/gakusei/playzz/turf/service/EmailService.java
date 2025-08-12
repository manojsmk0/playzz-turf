package com.example.gakusei.playzz.turf.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {
    void sendRegisteredEmail(String email,String userName);
    void sendConfirmBookingEmail(String email, String userName, LocalDate date, LocalTime startTime, LocalTime endTime);

    void sendConfirmCancelEmail(String email, String userName, LocalDate date, LocalTime startTime, LocalTime endTime);
    void sendRemainder(String email,String userName,LocalDate date,LocalTime startTime);

}
