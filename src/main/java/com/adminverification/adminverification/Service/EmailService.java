package com.adminverification.adminverification.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtp(String toEmail, String otp) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("sit.gauravn@gmail.com");
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP via email", e);

        }
    }
}

