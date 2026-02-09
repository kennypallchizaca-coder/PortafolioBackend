package com.lexisware.portafolio.project.repositories;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Repositorio de proyectos
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    // Busca proyectos por propietario
    Page<ProjectEntity> findByOwner_Uid(String uid, Pageable pageable);

    // Filtra proyectos por categoría
    Page<ProjectEntity> findByCategory(ProjectEntity.Category category, Pageable pageable);

    // Filtra proyectos por rol
    Page<ProjectEntity> findByRole(ProjectEntity.ProjectRole role, Pageable pageable);

    // Busca por propietario y categoría
    Page<ProjectEntity> findByOwner_UidAndCategory(String uid, ProjectEntity.Category category, Pageable pageable);

    // Cuenta proyectos por usuario
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.UserProjectCount(p.owner.displayName, COUNT(p)) FROM ProjectEntity p GROUP BY p.owner.displayName")
    List<UserProjectCount> countProjectsByUser();
}
