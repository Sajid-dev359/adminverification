package com.adminverification.adminverification.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logindata")
public class LoginData {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        private LocalDateTime loginTime;

    public LoginData() {
    }

    public LoginData(Long id, String email, LocalDateTime loginTime) {
        this.id = id;
        this.email = email;
        this.loginTime = loginTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }
}
