package com.adminverification.adminverification.Controller;

import com.adminverification.adminverification.Entity.AdminUser;
import com.adminverification.adminverification.Service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class    AuthController {
    @Autowired
    private AdminUserService adminUserService;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody Map<String, String> requestBody) {
//        String email = requestBody.get("email");
//        String password = requestBody.get("password");
//
//        try {
//            adminUserService.register(email, password);
//            return ResponseEntity.ok("Registration successful");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String password = requestBody.get("password");

        try {
            adminUserService.login(email, password);
            return ResponseEntity.ok("Login successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // Validate email format (additional step)
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        try {
            adminUserService.initiateForgotPassword(email);
            return ResponseEntity.ok("If the email exists in our records, an OTP has been sent to your email");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String otp = requestBody.get("otp");

        boolean isValid = adminUserService.verifyOtp(otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP or OTP expired");
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {
      String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");
        String confirmPassword = requestBody.get("confirmPassword");

        try {
            adminUserService.resetPassword(email, newPassword, confirmPassword);
            return ResponseEntity.ok("Password has been reset");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        // Implement email format validation logic if needed
        // For simplicity, assuming basic check
        return email != null && email.contains("@");
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
