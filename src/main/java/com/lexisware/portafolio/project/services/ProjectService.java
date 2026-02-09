package com.lexisware.portafolio.project.services;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.project.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

// Interfaz para gestión de proyectos
public interface ProjectService {

    // Obtiene todos los proyectos paginados
    Page<Project> obtenerTodosLosProyectos(Pageable pageable);

    // Obtiene lista completa de proyectos
    List<Project> obtenerTodosLosProyectos();

    // Busca proyecto por ID
    Project obtenerProyectoPorId(Long id);

    // Obtiene proyectos por propietario
    Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable);

    // Filtra proyectos por categoría
    Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable);

    // Filtra proyectos por rol
    Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable);

    // Crea proyecto
    Project crearProyecto(Project projectModel);

    // Actualiza proyecto
    Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid);

    // Elimina proyecto
    void eliminarProyecto(Long id, String requestUserUid);
}
