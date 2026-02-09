package com.lexisware.portafolio.users.models;

import java.time.LocalDateTime;
import java.util.List;

// Modelo de dominio que representa los atributos y lógica de negocio de un Usuario
public class User {
    private String uid;
    private String email;
    private String password; // Credencial de acceso para validaciones internas
    private String displayName;
    private Role role;
    private String specialty;
    private String bio;
    private String photoURL;
    private Boolean available;
    private List<String> skills;
    private List<String> schedule;

    // Referencias a perfiles externos del usuario
    private String github;
    private String instagram;
    private String whatsapp;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Perfiles y niveles de autorización dentro de la plataforma
    public enum Role {
        PROGRAMMER,
        ADMIN,
        EXTERNAL
    }

    public User() {
    }

    // Inicializa el modelo de dominio con la información personal, laboral y
    // técnica
    public User(String uid, String email, String password, String displayName, Role role, String specialty, String bio,
            String photoURL, Boolean available, List<String> skills, List<String> schedule, String github,
            String instagram, String whatsapp, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
        this.specialty = specialty;
        this.bio = bio;
        this.photoURL = photoURL;
        this.available = available;
        this.skills = skills;
        this.schedule = schedule;
        this.github = github;
        this.instagram = instagram;
        this.whatsapp = whatsapp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
