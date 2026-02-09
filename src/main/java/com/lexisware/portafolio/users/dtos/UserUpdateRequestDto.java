package com.lexisware.portafolio.users.dtos;

import jakarta.validation.constraints.*;

import java.util.List;

// Objeto de transferencia utilizado para actualizar campos específicos del perfil de usuario
public class UserUpdateRequestDto {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String displayName;

    @Size(max = 1000, message = "La biografía no puede exceder 1000 caracteres")
    private String bio;

    private String specialty;

    @Pattern(regexp = "^(https?://).*", message = "La URL de la foto debe ser válida")
    private String photoURL;

    private List<String> skills;

    private List<String> schedule;

    private Boolean available;

    // Enlaces opcionales a perfiles y redes sociales externos
    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String github;

    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String instagram;

    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String whatsapp;

    public UserUpdateRequestDto() {
    }

    // Inicializa el DTO de actualización con los nuevos datos profesionales y de
    // contacto
    public UserUpdateRequestDto(String displayName, String bio, String specialty, String photoURL, List<String> skills,
            List<String> schedule, Boolean available, String github, String instagram, String whatsapp) {
        this.displayName = displayName;
        this.bio = bio;
        this.specialty = specialty;
        this.photoURL = photoURL;
        this.skills = skills;
        this.schedule = schedule;
        this.available = available;
        this.github = github;
        this.instagram = instagram;
        this.whatsapp = whatsapp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
