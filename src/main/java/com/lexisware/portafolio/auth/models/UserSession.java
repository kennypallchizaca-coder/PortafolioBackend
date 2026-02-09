package com.lexisware.portafolio.auth.models;

import java.time.LocalDateTime;

// Modelo de dominio que representa una sesión de usuario vinculada a un token
public class UserSession {

    private String uid;
    private String email;
    private String token;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    public UserSession() {
    }

    public UserSession(String uid, String email, String token, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.uid = uid;
        this.email = email;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
