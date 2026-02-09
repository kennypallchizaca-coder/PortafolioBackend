package com.lexisware.portafolio.project.services;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.project.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

// Definición de las operaciones fundamentales para la administración de proyectos técnicos
public interface ProjectService {

    // Recupera un subconjunto de proyectos según los criterios de paginación
    // proporcionados
    Page<Project> obtenerTodosLosProyectos(Pageable pageable);

    // Obtiene el listado completo de todos los proyectos registrados en el sistema
    List<Project> obtenerTodosLosProyectos();

    // Localiza un proyecto específico utilizando su identificador técnico único ID
    Project obtenerProyectoPorId(Long id);

    // Lista paginada de proyectos filtrados por el identificador del propietario
    // UID
    Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable);

    // Filtra el catálogo de proyectos basándose en su clasificación principal
    Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable);

    // Filtra los proyectos registrados de acuerdo al rol desempeñado por su autor
    Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable);

    // Procesa y persiste un nuevo proyecto validando las reglas de integridad de
    // negocio
    Project crearProyecto(Project projectModel);

    // Modifica los atributos de un proyecto existente previa validación de permisos
    // del usuario
    Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid);

    // Remueve permanentemente un proyecto del sistema verificando la autoridad del
    // solicitante
    void eliminarProyecto(Long id, String requestUserUid);
}
