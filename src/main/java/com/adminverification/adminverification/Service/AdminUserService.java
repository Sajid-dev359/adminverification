package com.adminverification.adminverification.Service;
import com.adminverification.adminverification.Dao.AdminUserRepository;
import com.adminverification.adminverification.Dao.LoginDataRepository;
import com.adminverification.adminverification.Dao.OtpTokenRepository;
import com.adminverification.adminverification.Entity.AdminUser;
import com.adminverification.adminverification.Entity.LoginData;
import com.adminverification.adminverification.Entity.OtpToken;
import com.adminverification.adminverification.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private OtpTokenRepository otpTokenRepository;
    @Autowired
    private LoginDataRepository loginDataRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    public void register(String email, String password) {
//        if (adminUserRepository.findByEmail(email).isPresent()) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        String hashedPassword = passwordEncoder.encode(password);
//        AdminUser newUser = new AdminUser(email, hashedPassword); // Create AdminUser without otp
//        adminUserRepository.save(newUser);
//    }



    public void login(String email, String password) {
        AdminUser user = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

    }




    public Optional<AdminUser> findByEmail(String email) {
        return adminUserRepository.findByEmail(email);
    }

    public String  initiateForgotPassword(String email) {
        Optional<AdminUser> user = adminUserRepository.findByEmail(email);

        if (user.isPresent()) {
            Optional<OtpToken> existingOtpToken = otpTokenRepository.findByEmail(email);
            String otp;

            if (existingOtpToken.isPresent() && existingOtpToken.get().getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
                otp = existingOtpToken.get().getOtp(); // Use existing OTP if it's still valid
            } else {
                otp = generateOtp(); // Generate new OTP
                OtpToken otpToken = existingOtpToken.orElse(new OtpToken());
                otpToken.setEmail(email);
                otpToken.setOtp(otp);
                otpToken.setCreatedAt(LocalDateTime.now());
                otpTokenRepository.save(otpToken);
            }

            try {
                emailService.sendOtp(email, otp);
            } catch (Exception e) {
                // Handle exception from email service
                throw new RuntimeException("Failed to send OTP via email", e);
            }
            return "OTP sent to your email. Please check your inbox.";
        } else {
            throw new RuntimeException("Email not found");
        }
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<OtpToken> otpToken = otpTokenRepository.findByOtp( otp);

        if (otpToken.isPresent()) {
            if (otpToken.get().getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(120))) {
                otpTokenRepository.deleteByEmail(email); // Invalidate the OTP after use
                return true;
            } else {
                System.out.println("OTP expired");
            }
        } else {
            System.out.println("OTP not found or incorrect");
        }
        return false;
    }

    public boolean verifyOtp(String otp) {
        // Retrieve the logged-in user's email from the security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return verifyOtp(email, otp);
    }




    @Transactional
    public void resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        Optional<AdminUser> user = adminUserRepository.findByEmail(email);
        if (user.isPresent()) {
            AdminUser adminUser = user.get();
            adminUser.setPassword(passwordEncoder.encode(newPassword));
            adminUserRepository.save(adminUser);
        } else {
            throw new RuntimeException("Email not found");
        }
    }


    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
