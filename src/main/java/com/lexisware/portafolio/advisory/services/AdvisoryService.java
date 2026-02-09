package com.lexisware.portafolio.advisory.services;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.models.Advisory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Interfaz para gestión de asesorías
public interface AdvisoryService {

    // Obtiene todas las asesorías paginadas
    Page<Advisory> obtenerTodasLasAsesorias(Pageable pageable);

    // Busca asesoría por ID
    Advisory obtenerAsesoriaPorId(Long id);

    // Obtiene asesorías por programador
    Page<Advisory> obtenerAsesoriasPorProgramador(String programmerId, Pageable pageable);

    // Obtiene asesorías por email solicitante
    Page<Advisory> obtenerAsesoriasPorSolicitante(String email, Pageable pageable);

    // Filtra asesorías por estado
    Page<Advisory> obtenerAsesoriasPorEstado(AdvisoryEntity.Status status, Pageable pageable);

    // Crea asesoría
    Advisory crearAsesoria(Advisory advisoryModel);

    // Actualiza estado de asesoría
    Advisory actualizarEstadoAsesoria(Long id, Advisory.Status status, String requestUserUid);

    // Aprueba asesoría
    Advisory aprobarAsesoria(Long id, String requestUserUid);

    // Rechaza asesoría
    Advisory rechazarAsesoria(Long id, String requestUserUid);

    // Elimina asesoría
    void eliminarAsesoria(Long id, String requestUserUid);

    // Elimina historial de asesorías (completadas)
    void eliminarHistorial(String programmerUid);

    // Elimina historial de asesorías de solicitante (completadas)
    void eliminarHistorialSolicitante(String requesterEmail);
}
