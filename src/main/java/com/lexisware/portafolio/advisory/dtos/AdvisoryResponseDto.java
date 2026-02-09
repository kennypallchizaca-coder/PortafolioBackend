package com.lexisware.portafolio.advisory.dtos;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import java.time.LocalDateTime;

// DTO que representa la respuesta con los datos de una asesoría
public class AdvisoryResponseDto {

    private Long id; // Identificador de la asesoría
    private String programmerId; // Identificador del programador asignado
    private String programmerEmail; // Email del programador
    private String programmerName; // Nombre del programador
    private String requesterName; // Nombre del solicitante
    private String requesterEmail; // Email del solicitante
    private String date; // Fecha de la sesión
    private String time; // Hora de la sesión
    private String note; // Nota adicional
    private AdvisoryEntity.Status status; // Estado actual de la asesoría
    private LocalDateTime createdAt; // Fecha de creación del registro

    // Constructor vacío por defecto
    public AdvisoryResponseDto() {
    }

    // Constructor con todos los campos para instanciar la respuesta completa
    public AdvisoryResponseDto(Long id, String programmerId, String programmerEmail, String programmerName,
            String requesterName, String requesterEmail, String date, String time, String note,
            AdvisoryEntity.Status status, LocalDateTime createdAt) {
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

    public AdvisoryEntity.Status getStatus() {
        return status;
    }

    public void setStatus(AdvisoryEntity.Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
