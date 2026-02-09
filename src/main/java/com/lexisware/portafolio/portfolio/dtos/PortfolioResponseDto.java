package com.lexisware.portafolio.portfolio.dtos;

import java.time.LocalDateTime;

// DTO que representa un portafolio completo incluyendo sus proyectos y habilidades para la API
public class PortfolioResponseDto {

    private Long id;
    private String userId;
    private String title;
    private String description;
    private String theme;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private java.util.List<com.lexisware.portafolio.project.dtos.ProjectResponseDto> projects;
    private java.util.List<String> skills;

    public PortfolioResponseDto() {
    }

    // Inicializa el DTO de respuesta con toda la información detallada del
    // portafolio
    public PortfolioResponseDto(Long id, String userId, String title, String description, String theme,
            Boolean isPublic, LocalDateTime createdAt, LocalDateTime updatedAt,
            java.util.List<com.lexisware.portafolio.project.dtos.ProjectResponseDto> projects,
            java.util.List<String> skills) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.theme = theme;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projects = projects;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    public java.util.List<com.lexisware.portafolio.project.dtos.ProjectResponseDto> getProjects() {
        return projects;
    }

    public void setProjects(java.util.List<com.lexisware.portafolio.project.dtos.ProjectResponseDto> projects) {
        this.projects = projects;
    }

    public java.util.List<String> getSkills() {
        return skills;
    }

    public void setSkills(java.util.List<String> skills) {
        this.skills = skills;
    }
}
