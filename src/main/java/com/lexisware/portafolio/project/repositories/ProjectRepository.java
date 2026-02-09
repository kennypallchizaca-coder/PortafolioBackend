package com.lexisware.portafolio.project.repositories;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Interfaz para la manipulación persistente de proyectos mediante Spring Data JPA
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    // Recupera una página de proyectos que pertenecen a un usuario específico
    // mediante su UID
    Page<ProjectEntity> findByOwner_Uid(String uid, Pageable pageable);

    // Filtra los proyectos registrados según su categoría asignada (académico o
    // laboral)
    Page<ProjectEntity> findByCategory(ProjectEntity.Category category, Pageable pageable);

    // Obtiene proyectos cuyos desarrolladores desempeñaron un rol técnico
    // específico
    Page<ProjectEntity> findByRole(ProjectEntity.ProjectRole role, Pageable pageable);

    // Realiza una búsqueda combinada por identificador de usuario y categoría de
    // proyecto
    Page<ProjectEntity> findByOwner_UidAndCategory(String uid, ProjectEntity.Category category, Pageable pageable);

    // Genera estadísticas personalizadas contando los proyectos totales publicados
    // por cada usuario
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.UserProjectCount(p.owner.displayName, COUNT(p)) FROM ProjectEntity p GROUP BY p.owner.displayName")
    List<UserProjectCount> countProjectsByUser();
}
