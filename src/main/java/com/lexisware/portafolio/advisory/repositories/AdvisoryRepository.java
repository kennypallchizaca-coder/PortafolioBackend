package com.lexisware.portafolio.advisory.repositories;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Repositorio de asesorías
@Repository
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntity, Long> {

    // Busca asesorías por programador
    Page<AdvisoryEntity> findByProgrammerId(String programmerId, Pageable pageable);

    // Busca asesorías por email solicitante
    Page<AdvisoryEntity> findByRequesterEmail(String email, Pageable pageable);

    // Filtra asesorías por estado
    Page<AdvisoryEntity> findByStatus(AdvisoryEntity.Status status, Pageable pageable);

    // Cuenta asesorías por estado
    long countByStatus(AdvisoryEntity.Status status);

    // Busca asesorías por fecha
    List<AdvisoryEntity> findByDate(String date);

    // Busca asesorías para recordatorios (fecha, estado y no notificadas)
    List<AdvisoryEntity> findByDateAndStatusAndReminderSentFalse(String date, AdvisoryEntity.Status status);

    // Agrupa asesorías por mes
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(CONCAT(TO_CHAR(a.createdAt, 'Mon'), ' ', TO_CHAR(a.createdAt, 'YYYY')), COUNT(a)) FROM AdvisoryEntity a GROUP BY TO_CHAR(a.createdAt, 'Mon'), TO_CHAR(a.createdAt, 'YYYY'), EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt) ORDER BY EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt)")
    List<AdvisoryStatsDto> countAdvisoriesByMonth();

    // Agrupa asesorías por programador
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(a.programmerName, COUNT(a)) FROM AdvisoryEntity a GROUP BY a.programmerName ORDER BY COUNT(a) DESC")
    List<AdvisoryStatsDto> countAdvisoriesByProgrammer();

    // Elimina asesorías por programador y estado
    void deleteByProgrammerIdAndStatusIn(String programmerId, java.util.Collection<AdvisoryEntity.Status> statuses);

    // Elimina asesorías por solicitante y estado
    void deleteByRequesterEmailAndStatusIn(String requesterEmail, java.util.Collection<AdvisoryEntity.Status> statuses);
}
