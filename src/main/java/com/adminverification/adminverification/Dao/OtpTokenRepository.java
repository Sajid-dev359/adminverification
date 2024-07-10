package com.adminverification.adminverification.Dao;

import com.adminverification.adminverification.Entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken,Long> {

//    Optional<OtpToken>findByEmailAndOtp(String email,String otp);
    void deleteByEmail(String email);
    Optional<OtpToken> findByEmail(String email);
    Optional<OtpToken> findByOtp(String otp); //New method to find by OTP alone

}
