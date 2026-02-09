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

// Implementación de la lógica de negocio para la gestión de ciclo de vida de proyectos
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    // Inicializa el servicio inyectando el repositorio de datos y el conversor de
    // objetos
    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    // Retorna una página de proyectos transformados a modelos de dominio
    @Override
    public Page<Project> obtenerTodosLosProyectos(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toModel);
    }

    // Recupera la totalidad de proyectos registrados en la base de datos
    @Override
    public List<Project> obtenerTodosLosProyectos() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toModel)
                .toList();
    }

    // Busca un proyecto por su identificadorID, lanzando excepción si no se
    // encuentra
    @Override
    public Project obtenerProyectoPorId(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));
        return projectMapper.toModel(entity);
    }

    // Obtiene el catálogo paginado de proyectos asociados a un UID de usuario
    // específico
    @Override
    public Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable) {
        return projectRepository.findByOwner_Uid(uid, pageable)
                .map(projectMapper::toModel);
    }

    // Filtra proyectos por su origen (académico/laboral) mediante paginación
    @Override
    public Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable) {
        return projectRepository.findByCategory(category, pageable)
                .map(projectMapper::toModel);
    }

    // Consulta proyectos registrados bajo un perfil técnico específico
    @Override
    public Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable) {
        return projectRepository.findByRole(role, pageable)
                .map(projectMapper::toModel);
    }

    // Persiste un nuevo proyecto sincronizando el nombre del programador desde el
    // propietario
    @Override
    @Transactional
    public Project crearProyecto(Project projectModel) {
        ProjectEntity entity = projectMapper.toEntity(projectModel);

        // Copia el nombre visible del usuario propietario a la entidad del proyecto
        if (projectModel.getOwner() != null) {
            entity.setProgrammerName(projectModel.getOwner().getDisplayName());
        }

        ProjectEntity saved = projectRepository.save(entity);
        return projectMapper.toModel(saved);
    }

    // Verifica si el usuario actual tiene privilegios de administrador o es el
    // dueño del recurso
    private void validarPropiedad(ProjectEntity project, String appUserUid) {
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return; // Los administradores tienen acceso total
        }

        // Valida que el UID del usuario solicitante coincida con el UID del propietario
        if (project.getOwner() != null && project.getOwner().getUid().equals(appUserUid)) {
            return;
        }

        throw new AccessDeniedException(
                "No tienes permisos para modificar este proyecto");
    }

    // Modifica un proyecto existente aplicando cambios parciales y validando
    // autoría
    @Override
    @Transactional
    public Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid) {
        ProjectEntity existingEntity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        validarPropiedad(existingEntity, requestUserUid);

        // Actualización condicional de campos mutables del proyecto
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

        ProjectEntity saved = projectRepository.save(existingEntity);
        return projectMapper.toModel(saved);
    }

    // Elimina un registro de proyecto tras confirmar que el usuario tiene permisos
    // suficientes
    @Override
    @Transactional
    public void eliminarProyecto(Long id, String requestUserUid) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        validarPropiedad(entity, requestUserUid);

        projectRepository.delete(entity);
    }
}
