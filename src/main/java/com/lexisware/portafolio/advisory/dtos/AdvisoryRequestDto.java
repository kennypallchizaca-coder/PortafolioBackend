package com.lexisware.portafolio.advisory.dtos;

import jakarta.validation.constraints.*;

// DTO de solicitud de asesoría
public class AdvisoryRequestDto {

    @NotBlank(message = "El ID del programador es obligatorio")
    private String programmerId;

    @NotBlank(message = "El email del programador es obligatorio")
    @Email(message = "El email del programador debe ser válido")
    private String programmerEmail;

    @NotBlank(message = "El nombre del programador es obligatorio")
    private String programmerName;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String requesterName;

    @NotBlank(message = "El email del solicitante es obligatorio")
    @Email(message = "El email del solicitante debe ser válido")
    private String requesterEmail;

    @NotBlank(message = "La fecha de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD")
    private String date;

    @NotBlank(message = "La hora de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "La hora debe tener formato HH:MM")
    private String time;

    @Size(max = 1000, message = "La nota no puede exceder 1000 caracteres")
    private String note;

    public AdvisoryRequestDto() {
    }

    public AdvisoryRequestDto(String programmerId, String programmerEmail, String programmerName, String requesterName,
            String requesterEmail, String date, String time, String note) {
        this.programmerId = programmerId;
        this.programmerEmail = programmerEmail;
        this.programmerName = programmerName;
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.date = date;
        this.time = time;
        this.note = note;
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
}
