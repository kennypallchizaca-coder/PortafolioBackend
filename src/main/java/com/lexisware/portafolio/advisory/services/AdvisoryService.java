package com.lexisware.portafolio.advisory.services;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.models.Advisory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Interfaz para definir la lógica de negocio de las asesorías
public interface AdvisoryService {

    // Obtiene todas las asesorías registradas de forma paginada
    Page<Advisory> obtenerTodasLasAsesorias(Pageable pageable);

    // Obtiene los detalles de una asesoría específica por su ID
    Advisory obtenerAsesoriaPorId(Long id);

    // Obtiene las asesorías asignadas a un programador específico
    Page<Advisory> obtenerAsesoriasPorProgramador(String programmerId, Pageable pageable);

    // Obtiene las asesorías solicitadas por un email específico
    Page<Advisory> obtenerAsesoriasPorSolicitante(String email, Pageable pageable);

    // Filtra las asesorías según su estado actual
    Page<Advisory> obtenerAsesoriasPorEstado(AdvisoryEntity.Status status, Pageable pageable);

    // Crea una nueva asesoría y su notificación correspondiente
    Advisory crearAsesoria(Advisory advisoryModel);

    // Actualiza el estado de una asesoría existente
    Advisory actualizarEstadoAsesoria(Long id, Advisory.Status status, String requestUserUid);

    // Aprueba una asesoría pendiente
    Advisory aprobarAsesoria(Long id, String requestUserUid);

    // Rechaza una asesoría pendiente
    Advisory rechazarAsesoria(Long id, String requestUserUid);

    // Elimina una asesoría del sistema
    void eliminarAsesoria(Long id, String requestUserUid);

    // Elimina todo el historial de asesorías finalizadas de un programador
    void eliminarHistorial(String programmerUid);

    // Elimina el historial de asesorías completadas o rechazadas de un solicitante
    void eliminarHistorialSolicitante(String email);
}
