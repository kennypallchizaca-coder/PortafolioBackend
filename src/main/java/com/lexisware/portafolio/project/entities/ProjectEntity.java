package com.lexisware.portafolio.project.entities;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

// Representación persistente de un proyecto dentro de la base de datos
@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el usuario propietario registrado en el sistema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({ "password", "projects", "advisoriesAsProgrammer" })
    private UserEntity owner;

    // Relación opcional con un portafolio profesional específico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnoreProperties({ "projects", "user" })
    private PortfolioEntity portfolio;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    // Listado de tecnologías utilizadas en la implementación del proyecto
    @ElementCollection
    @CollectionTable(name = "project_tech_stack", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> techStack;

    private String repoUrl;
    private String demoUrl;
    private String imageUrl;

    @Column(name = "programmer_name")
    private String programmerName; // Almacena el nombre del creador para acceso rápido

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Clasificación del proyecto según su origen
    public enum Category {
        academico,
        laboral
    }

    // Rol técnico principal desempeñado durante el desarrollo
    public enum ProjectRole {
        frontend,
        backend,
        fullstack,
        db
    }

    public ProjectEntity() {
    }

    // Inicializa la entidad con todos sus atributos requeridos para la persistencia
    // JPA
    public ProjectEntity(Long id, UserEntity owner, PortfolioEntity portfolio, String title, String description,
            Category category, ProjectRole role, List<String> techStack, String repoUrl, String demoUrl,
            String imageUrl, String programmerName, LocalDateTime createdAt) {
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectEntity that = (ProjectEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", role=" + role +
                ", techStack=" + techStack +
                ", repoUrl='" + repoUrl + '\'' +
                ", demoUrl='" + demoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", programmerName='" + programmerName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
