package com.lexisware.portafolio.files.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad para registrar el historial de archivos subidos al sistema
@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String folder;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public FileEntity() {
    }

    public FileEntity(String url, String publicId, String folder) {
        this.url = url;
        this.publicId = publicId;
        this.folder = folder;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
