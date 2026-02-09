package com.lexisware.portafolio.auth.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad para auditar los intentos de inicio de sesión en el sistema
@Entity
@Table(name = "auth_logs")
public class AuthLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean success;

    @Column
    private String clientIp;

    public AuthLogEntity() {
    }

    public AuthLogEntity(String email, boolean success, String clientIp) {
        this.email = email;
        this.success = success;
        this.clientIp = clientIp;
        this.timestamp = LocalDateTime.now();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
