package com.lexisware.portafolio.advisory.models;

import java.time.LocalDateTime;

// Modelo de dominio para representar una sesión de Asesoría técnica
public class Advisory {
    private Long id;
    private String programmerId;
    private String programmerEmail;
    private String programmerName;
    private String requesterName;
    private String requesterEmail;
    private String date;
    private String time;
    private String note;
    private Status status;
    private LocalDateTime createdAt;

    public Advisory() {
    }

    // Constructor completo para inicialización
    public Advisory(Long id, String programmerId, String programmerEmail, String programmerName, String requesterName,
            String requesterEmail, String date, String time, String note, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.programmerId = programmerId;
        this.programmerEmail = programmerEmail;
        this.programmerName = programmerName;
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.date = date;
        this.time = time;
        this.note = note;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgrammerId() {
        return programmerId;
    }

    public void setProgrammerId(String programmerId) {
        this.programmerId = programmerId;
    }

    public String getProgrammerEmail() {
        return programmerEmail;
    }

    public void setProgrammerEmail(String programmerEmail) {
        this.programmerEmail = programmerEmail;
    }

    public String getProgrammerName() {
        return programmerName;
    }

    public void setProgrammerName(String programmerName) {
        this.programmerName = programmerName;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Enumeración de estados posibles de una asesoría
    public enum Status {
        pending,
        approved,
        rejected
    }
}
