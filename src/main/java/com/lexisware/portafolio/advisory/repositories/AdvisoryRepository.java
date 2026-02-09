package com.lexisware.portafolio.advisory.repositories;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Repositorio JPA para gestionar las operaciones de base de datos de Asesorías
@Repository
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntity, Long> {

    // Encuentra asesorías asignadas a un programador específico
    Page<AdvisoryEntity> findByProgrammerId(String programmerId, Pageable pageable);

    // Encuentra asesorías solicitadas por un email específico
    Page<AdvisoryEntity> findByRequesterEmail(String email, Pageable pageable);

    // Encuentra asesorías filtradas por su estado actual
    Page<AdvisoryEntity> findByStatus(AdvisoryEntity.Status status, Pageable pageable);

    // Cuenta el número total de asesorías en un estado específico
    long countByStatus(AdvisoryEntity.Status status);

    // Encuentra asesorías programadas para una fecha específica
    List<AdvisoryEntity> findByDate(String date);

    // Encuentra asesorías pendientes de notificación para una fecha y estado
    List<AdvisoryEntity> findByDateAndStatusAndReminderSentFalse(String date, AdvisoryEntity.Status status);

    // Agrupa y cuenta asesorías por mes de creación
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(CONCAT(TO_CHAR(a.createdAt, 'Mon'), ' ', TO_CHAR(a.createdAt, 'YYYY')), COUNT(a)) FROM AdvisoryEntity a GROUP BY TO_CHAR(a.createdAt, 'Mon'), TO_CHAR(a.createdAt, 'YYYY'), EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt) ORDER BY EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt)")
    List<AdvisoryStatsDto> countAdvisoriesByMonth();

    // Agrupa y cuenta asesorías por programador
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(a.programmerName, COUNT(a)) FROM AdvisoryEntity a GROUP BY a.programmerName ORDER BY COUNT(a) DESC")
    List<AdvisoryStatsDto> countAdvisoriesByProgrammer();

    // Elimina asesorías de un programador con estados específicos
    void deleteByProgrammerIdAndStatusIn(String programmerId, java.util.Collection<AdvisoryEntity.Status> statuses);

    // Elimina asesorías de un solicitante con estados específicos
    void deleteByRequesterEmailAndStatusIn(String requesterEmail, java.util.Collection<AdvisoryEntity.Status> statuses);
}
