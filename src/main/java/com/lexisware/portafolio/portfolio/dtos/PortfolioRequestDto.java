package com.lexisware.portafolio.portfolio.dtos;

import jakarta.validation.constraints.*;

// DTO para la creación o actualización de los datos básicos de un portafolio
public class PortfolioRequestDto {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String userId;

    @NotBlank(message = "El título del portafolio es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    private String theme;
    private Boolean isPublic = true;
    private java.util.List<String> skills;

    public PortfolioRequestDto() {
    }

    // Inicializa el DTO con los campos fundamentales del portafolio
    public PortfolioRequestDto(String userId, String title, String description, String theme, Boolean isPublic,
            java.util.List<String> skills) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.theme = theme;
        this.isPublic = isPublic;
        this.skills = skills;
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

    public java.util.List<String> getSkills() {
        return skills;
    }

    public void setSkills(java.util.List<String> skills) {
        this.skills = skills;
    }
}
