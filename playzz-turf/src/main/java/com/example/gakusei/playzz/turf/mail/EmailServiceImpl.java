package com.example.gakusei.playzz.turf.mail;

import com.example.gakusei.playzz.turf.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegisteredEmail(String email, String userName){
        System.out.println("Registration mail sent");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Regarding registration on playzzturf");
        String body = "Dear "+userName+ ",\n\n"
                        +"You can login using the username and password that you set while registering"
                            +"Thank you! for supporting playzzturf . Have a good day";
        message.setText(body);
        javaMailSender.send(message);
    }
    public void sendConfirmBookingEmail(String email, String userName, LocalDate date, LocalTime startTime, LocalTime endTime)
    {
        System.out.println("Confirm Booking Mail sent");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Slot Booked confirmation");

        String body = "Dear " + userName + ",\n\n"
                + "Your booking for the turf slot on " + date + " from " + startTime + " to " + endTime + " has been confirmed.\n\n"
                + "Thank you for supporting playzzturf. Have a great day!";
        message.setText(body);
        javaMailSender.send(message);
    }
    public void sendConfirmCancelEmail(String email, String userName, LocalDate date, LocalTime startTime, LocalTime endTime){
        System.out.println("Confirm cancel Mail sent");
        SimpleMailMessage message =new SimpleMailMessage();
        message.setTo(email);
         message.setSubject("Slot Cancel confirmation");

        String body = "Dear " + userName + ",\n\n"
                + "Your cancellation for the turf slot on " + date + " from " + startTime + " to " + endTime + " has been confirmed.\n\n"
                + "Thank you for supporting playzzturf. Have a great day!";
         message.setText(body);
         javaMailSender.send(message);
    }
    public void sendRemainder(String email,String userName,LocalDate date,LocalTime startTime){
        System.out.println("Remainder Mail sent ");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Remainder for Your booked turf slot");
        String body = "Remainder for your booked slot"
         +"Dear "+ userName + ",\n\n"
                +", Your Booked slot starts on:"+date+"at"+startTime+",\n\n"
        +"Thank you! for supporting playzzturf . Have a good day";
        message.setText(body);
        javaMailSender.send(message);
    }

}
