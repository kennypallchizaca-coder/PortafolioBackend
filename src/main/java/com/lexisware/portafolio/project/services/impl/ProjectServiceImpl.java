package com.lexisware.portafolio.project.services.impl;

import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.project.mappers.ProjectMapper;
import com.lexisware.portafolio.project.models.Project;
import com.lexisware.portafolio.project.services.ProjectService;
import com.lexisware.portafolio.exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Servicio de gestión de proyectos
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    // Obtiene todos los proyectos paginados
    @Override
    public Page<Project> obtenerTodosLosProyectos(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toModel);
    }

    // Retorna lista completa de proyectos
    @Override
    public List<Project> obtenerTodosLosProyectos() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toModel)
                .toList();
    }

    // Busca proyecto por ID
    @Override
    public Project obtenerProyectoPorId(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));
        return projectMapper.toModel(entity);
    }

    // Obtiene proyectos por propietario
    @Override
    public Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable) {
        return projectRepository.findByOwner_Uid(uid, pageable)
                .map(projectMapper::toModel);
    }

    // Filtra proyectos por categoría
    @Override
    public Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable) {
        return projectRepository.findByCategory(category, pageable)
                .map(projectMapper::toModel);
    }

    // Filtra proyectos por rol
    @Override
    public Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable) {
        return projectRepository.findByRole(role, pageable)
                .map(projectMapper::toModel);
    }

    // Crea nuevo proyecto
    @Override
    @Transactional
    public Project crearProyecto(Project projectModel) {
        // Convierte modelo a entidad
        ProjectEntity entity = projectMapper.toEntity(projectModel);

        // Sincroniza nombre del programador
        if (projectModel.getOwner() != null) {
            entity.setProgrammerName(projectModel.getOwner().getDisplayName());
        }

        ProjectEntity saved = projectRepository.save(entity);
        return projectMapper.toModel(saved);
    }

    // Valida propiedad del proyecto
    private void validarPropiedad(ProjectEntity project, String appUserUid) {
        // Verifica admin
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return; // Admin OK
        }

        // Verifica propietario
        if (project.getOwner() != null && project.getOwner().getUid().equals(appUserUid)) {
            return; // Propietario OK
        }

        // Acceso denegado
        throw new AccessDeniedException(
                "No tienes permisos para modificar este proyecto");
    }

    // Actualiza proyecto
    @Override
    @Transactional
    public Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid) {
        // Busca proyecto
        ProjectEntity existingEntity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // Valida permisos
        validarPropiedad(existingEntity, requestUserUid);

        // Actualiza campos parciales
        if (projectUpdateModel.getTitle() != null)
            existingEntity.setTitle(projectUpdateModel.getTitle());
        if (projectUpdateModel.getDescription() != null)
            existingEntity.setDescription(projectUpdateModel.getDescription());
        if (projectUpdateModel.getCategory() != null)
            existingEntity.setCategory(ProjectEntity.Category.valueOf(projectUpdateModel.getCategory().name()));
        if (projectUpdateModel.getRole() != null)
            existingEntity.setRole(ProjectEntity.ProjectRole.valueOf(projectUpdateModel.getRole().name()));
        if (projectUpdateModel.getTechStack() != null)
            existingEntity.setTechStack(projectUpdateModel.getTechStack());
        if (projectUpdateModel.getRepoUrl() != null)
            existingEntity.setRepoUrl(projectUpdateModel.getRepoUrl());
        if (projectUpdateModel.getDemoUrl() != null)
            existingEntity.setDemoUrl(projectUpdateModel.getDemoUrl());
        if (projectUpdateModel.getImageUrl() != null)
            existingEntity.setImageUrl(projectUpdateModel.getImageUrl());

        // Guarda cambios
        ProjectEntity saved = projectRepository.save(existingEntity);
        return projectMapper.toModel(saved);
    }

    // Elimina proyecto
    @Override
    @Transactional
    public void eliminarProyecto(Long id, String requestUserUid) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // Valida permisos
        validarPropiedad(entity, requestUserUid);

        projectRepository.delete(entity);
    }
}
