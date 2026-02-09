package com.lexisware.portafolio.advisory.dtos;

import jakarta.validation.constraints.*;

// DTO para manejar la solicitud de creación de una nueva asesoría
public class AdvisoryRequestDto {

    @NotBlank(message = "El ID del programador es obligatorio")
    private String programmerId; // Identificador único del programador

    @NotBlank(message = "El email del programador es obligatorio")
    @Email(message = "El email del programador debe ser válido")
    private String programmerEmail; // Correo electrónico del programador

    @NotBlank(message = "El nombre del programador es obligatorio")
    private String programmerName; // Nombre del programador

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String requesterName; // Nombre de la persona que solicita la asesoría

    @NotBlank(message = "El email del solicitante es obligatorio")
    @Email(message = "El email del solicitante debe ser válido")
    private String requesterEmail; // Correo electrónico del solicitante

    @NotBlank(message = "La fecha de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD")
    private String date; // Fecha programada para la asesoría

    @NotBlank(message = "La hora de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "La hora debe tener formato HH:MM")
    private String time; // Hora programada para la asesoría

    @Size(max = 1000, message = "La nota no puede exceder 1000 caracteres")
    private String note; // Notas o descripción adicional de la solicitud

    // Constructor vacío por defecto
    public AdvisoryRequestDto() {
    }

    // Constructor con todos los campos para inicialización completa
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
