package com.lexisware.portafolio.project.models;

import java.time.LocalDateTime;
import java.util.List;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.portfolio.models.Portfolio;

// Modelo de dominio que representa un Proyecto en la lógica de negocio
// Modelo de dominio que representa un Proyecto en la lógica de negocio
public class Project {
    private Long id;
    private User owner;
    private Portfolio portfolio;
    private String title;
    private String description;
    private Category category;
    private ProjectRole role;
    private List<String> techStack;
    private String repoUrl;
    private String demoUrl;
    private String imageUrl;
    private String programmerName;
    private LocalDateTime createdAt;

    public Project() {
    }

    public Project(Long id, User owner, Portfolio portfolio, String title, String description, Category category,
            ProjectRole role, List<String> techStack, String repoUrl, String demoUrl, String imageUrl,
            String programmerName, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.portfolio = portfolio;
        this.title = title;
        this.description = description;
        this.category = category;
        this.role = role;
        this.techStack = techStack;
        this.repoUrl = repoUrl;
        this.demoUrl = demoUrl;
        this.imageUrl = imageUrl;
        this.programmerName = programmerName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
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

    public String getProgrammerName() {
        return programmerName;
    }

    public void setProgrammerName(String programmerName) {
        this.programmerName = programmerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Categoría del proyecto para filtrado
    public enum Category {
        academico,
        laboral
    }

    // Roles disponibles en el desarrollo
    public enum ProjectRole {
        frontend,
        backend,
        fullstack,
        db
    }
}
