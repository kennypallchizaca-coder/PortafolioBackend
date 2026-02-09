package com.lexisware.portafolio.project.dtos;

import com.lexisware.portafolio.project.entities.ProjectEntity;

import java.time.LocalDateTime;
import java.util.List;

// DTO de respuesta de proyecto
public class ProjectResponseDto {

    private Long id;
    private String title;
    private String description;
    private ProjectEntity.Category category;
    private ProjectEntity.ProjectRole role;
    private List<String> techStack;
    private String repoUrl;
    private String demoUrl;
    private String imageUrl;
    private String programmerName;
    private LocalDateTime createdAt;

    // Info. reducida del propietario
    private OwnerDto owner;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(Long id, String title, String description, ProjectEntity.Category category,
            ProjectEntity.ProjectRole role, List<String> techStack, String repoUrl, String demoUrl, String imageUrl,
            String programmerName, LocalDateTime createdAt, OwnerDto owner) {
        this.id = id;
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
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OwnerDto getOwner() {
        return owner;
    }

    public void setOwner(OwnerDto owner) {
        this.owner = owner;
    }

    public static class OwnerDto {
        private String uid;
        private String displayName;
        private String photoURL;

        public OwnerDto() {
        }

        public OwnerDto(String uid, String displayName, String photoURL) {
            this.uid = uid;
            this.displayName = displayName;
            this.photoURL = photoURL;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }
    }
}
