package com.lexisware.portafolio.advisory.services.impl;

import com.lexisware.portafolio.utils.EmailService;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.mappers.AdvisoryMapper;
import com.lexisware.portafolio.advisory.models.Advisory;
import com.lexisware.portafolio.advisory.services.AdvisoryService;
import com.lexisware.portafolio.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Servicio de gestión de asesorías y notificaciones
@Service
public class AdvisoryServiceImpl implements AdvisoryService {

    private static final Logger log = LoggerFactory.getLogger(AdvisoryServiceImpl.class);

    private final AdvisoryRepository advisoryRepository;
    private final AdvisoryMapper advisoryMapper;
    private final EmailService emailService;

    public AdvisoryServiceImpl(AdvisoryRepository advisoryRepository, AdvisoryMapper advisoryMapper,
            EmailService emailService) {
        this.advisoryRepository = advisoryRepository;
        this.advisoryMapper = advisoryMapper;
        this.emailService = emailService;
    }

    // Obtiene todas las asesorías paginadas
    @Override
    public Page<Advisory> obtenerTodasLasAsesorias(Pageable pageable) {
        return advisoryRepository.findAll(pageable)
                .map(advisoryMapper::toModel);
    }

    // Busca asesoría por ID
    @Override
    public Advisory obtenerAsesoriaPorId(Long id) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));
        return advisoryMapper.toModel(entity);
    }

    // Obtiene asesorías por programador
    @Override
    public Page<Advisory> obtenerAsesoriasPorProgramador(String programmerId, Pageable pageable) {
        return advisoryRepository.findByProgrammerId(programmerId, pageable)
                .map(advisoryMapper::toModel);
    }

    // Busca asesorías por email del solicitante
    @Override
    public Page<Advisory> obtenerAsesoriasPorSolicitante(String email, Pageable pageable) {
        return advisoryRepository.findByRequesterEmail(email, pageable)
                .map(advisoryMapper::toModel);
    }

    // Filtra asesorías por estado
    @Override
    public Page<Advisory> obtenerAsesoriasPorEstado(AdvisoryEntity.Status status, Pageable pageable) {
        return advisoryRepository.findByStatus(status, pageable)
                .map(advisoryMapper::toModel);
    }

    // Crea asesoría y envía notificaciones
    @Override
    @Transactional
    public Advisory crearAsesoria(Advisory advisoryModel) {
        // Estado inicial
        advisoryModel.setStatus(Advisory.Status.pending);

        AdvisoryEntity entity = advisoryMapper.toEntity(advisoryModel);
        AdvisoryEntity savedAdvisory = advisoryRepository.save(entity);

        // Notifica al programador
        try {
            emailService.sendAdvisoryNotificationToProgrammer(
                    savedAdvisory.getProgrammerEmail(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime(),
                    savedAdvisory.getNote());
        } catch (Exception e) {
            log.error("Error al enviar email informativo al programador: {}", e.getMessage());
        }

        // Notifica al solicitante
        try {
            emailService.sendAdvisoryConfirmationToRequester(
                    savedAdvisory.getRequesterEmail(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime());
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación al solicitante: {}", e.getMessage());
        }

        return advisoryMapper.toModel(savedAdvisory);
    }

    // Valida permisos de gestión
    private void validarPropiedad(AdvisoryEntity advisory, String appUserUid) {
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin || (advisory.getProgrammerId() != null && advisory.getProgrammerId().equals(appUserUid))) {
            return;
        }

        throw new AccessDeniedException(
                "No tienes permisos para gestionar esta asesoría");
    }

    // Actualiza estado y notifica
    @Override
    @Transactional
    public Advisory actualizarEstadoAsesoria(Long id, Advisory.Status status, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // Valida permisos
        validarPropiedad(entity, requestUserUid);

        // Actualiza estado
        entity.setStatus(AdvisoryEntity.Status.valueOf(status.name()));

        AdvisoryEntity updatedAdvisory = advisoryRepository.save(entity);

        // Notifica al solicitante
        try {
            emailService.sendAdvisoryStatusUpdate(
                    updatedAdvisory.getRequesterEmail(),
                    updatedAdvisory.getRequesterName(),
                    status.name().toLowerCase(),
                    updatedAdvisory.getProgrammerName());
        } catch (Exception e) {
            log.error("Error al enviar email de actualización de estado: {}", e.getMessage());
        }

        return advisoryMapper.toModel(updatedAdvisory);
    }

    // Aprueba asesoría
    @Override
    @Transactional
    public Advisory aprobarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.approved, requestUserUid);
    }

    // Rechaza asesoría
    @Override
    @Transactional
    public Advisory rechazarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.rejected, requestUserUid);
    }

    // Elimina asesoría por ID
    @Override
    @Transactional
    public void eliminarAsesoria(Long id, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // Valida permisos
        validarPropiedad(entity, requestUserUid);

        advisoryRepository.deleteById(id);
    }

    // Elimina historial de asesorías (completadas)
    @Override
    @Transactional
    public void eliminarHistorial(String programmerUid) {
        advisoryRepository.deleteByProgrammerIdAndStatusIn(
                programmerUid,
                java.util.List.of(AdvisoryEntity.Status.approved, AdvisoryEntity.Status.rejected));
    }

    // Elimina historial de asesorías de solicitante (completadas)
    @Override
    @Transactional
    public void eliminarHistorialSolicitante(String requesterEmail) {
        advisoryRepository.deleteByRequesterEmailAndStatusIn(
                requesterEmail,
                java.util.List.of(AdvisoryEntity.Status.approved, AdvisoryEntity.Status.rejected));
    }
}
