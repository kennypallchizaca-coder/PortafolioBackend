package com.lexisware.portafolio.project.dtos;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.validation.constraints.*;

import java.util.List;

// DTO de solicitud de proyecto
public class ProjectRequestDto {

    @NotBlank(message = "El título del proyecto es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción del proyecto es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String description;

    @NotNull(message = "La categoría del proyecto es obligatoria")
    private ProjectEntity.Category category;

    @NotNull(message = "El rol en el proyecto es obligatorio")
    private ProjectEntity.ProjectRole role;

    private List<String> techStack;

    @Pattern(regexp = "^(https?://).*", message = "La URL del repositorio debe ser válida")
    private String repoUrl;

    @Pattern(regexp = "^(https?://).*", message = "La URL de la demo debe ser válida")
    private String demoUrl;

    private String imageUrl;

    private String ownerUid;

    private Long portfolioId;

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(String title, String description, ProjectEntity.Category category,
            ProjectEntity.ProjectRole role, List<String> techStack, String repoUrl, String demoUrl, String imageUrl,
            String ownerUid, Long portfolioId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.role = role;
        this.techStack = techStack;
        this.repoUrl = repoUrl;
        this.demoUrl = demoUrl;
        this.imageUrl = imageUrl;
        this.ownerUid = ownerUid;
        this.portfolioId = portfolioId;
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

    public ProjectEntity.Category getCategory() {
        return category;
    }

    public void setCategory(ProjectEntity.Category category) {
        this.category = category;
    }

    public ProjectEntity.ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectEntity.ProjectRole role) {
        this.role = role;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
