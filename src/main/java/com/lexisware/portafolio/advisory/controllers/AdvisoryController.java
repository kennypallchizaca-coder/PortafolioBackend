package com.lexisware.portafolio.advisory.controllers;

import com.lexisware.portafolio.advisory.services.AdvisoryService;
import com.lexisware.portafolio.advisory.mappers.AdvisoryMapper;
import com.lexisware.portafolio.advisory.dtos.AdvisoryRequestDto;
import com.lexisware.portafolio.advisory.dtos.AdvisoryResponseDto;
import com.lexisware.portafolio.advisory.models.Advisory;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

// Controlador REST para gestionar las asesorías técnicas
@RestController
@RequestMapping("/api/advisories")
public class AdvisoryController {

    private final AdvisoryService advisoryService;
    private final AdvisoryMapper advisoryMapper;

    // Inicializa el controlador con los servicios necesarios
    public AdvisoryController(AdvisoryService advisoryService, AdvisoryMapper advisoryMapper) {
        this.advisoryService = advisoryService;
        this.advisoryMapper = advisoryMapper;
    }

    // Obtiene todas las asesorías (Solo Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerTodasLasAsesorias(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerTodasLasAsesorias(pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene una asesoría específica por su ID
    @GetMapping("/{id}")
    public ResponseEntity<AdvisoryResponseDto> obtenerAsesoriaPorId(@PathVariable("id") Long id) {
        Advisory advisory = advisoryService.obtenerAsesoriaPorId(id);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(advisory));
    }

    // Obtiene las asesorías asignadas al programador autenticado
    @GetMapping("/my-advisories")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerMisAsesorias(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(uid, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene las asesorías de un programador específico
    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorProgramador(
            @PathVariable("programmerId") String programmerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(programmerId, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene las asesorías solicitadas por un email específico
    @GetMapping("/requester/{email}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorSolicitante(
            @PathVariable("email") String email,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorSolicitante(email, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Filtra las asesorías por su estado actual
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorEstado(
            @PathVariable("status") AdvisoryEntity.Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorEstado(status, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Crea una nueva solicitud de asesoría
    @PostMapping
    public ResponseEntity<AdvisoryResponseDto> crearAsesoria(@Valid @RequestBody AdvisoryRequestDto request) {
        Advisory advisoryModel = advisoryMapper.toModel(request);
        Advisory created = advisoryService.crearAsesoria(advisoryModel);
        return new ResponseEntity<>(advisoryMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Actualiza el estado de una asesoría (pendiente, aprobada, rechazada)
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvisoryResponseDto> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal String uid) {
        String statusStr = body.get("status");
        Advisory.Status status = Advisory.Status.valueOf(statusStr);
        Advisory updated = advisoryService.actualizarEstadoAsesoria(id, status, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(updated));
    }

    // Aprueba una asesoría existente
    @PatchMapping("/{id}/approve")
    public ResponseEntity<AdvisoryResponseDto> aprobarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory approved = advisoryService.aprobarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(approved));
    }

    // Rechaza una asesoría existente
    @PatchMapping("/{id}/reject")
    public ResponseEntity<AdvisoryResponseDto> rechazarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory rejected = advisoryService.rechazarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(rejected));
    }

    // Elimina una asesoría por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarAsesoria(id, uid);
        return ResponseEntity.noContent().build();
    }

    // Elimina todo el historial de asesorías completadas del usuario actual
    @DeleteMapping("/history")
    public ResponseEntity<Void> eliminarHistorial(
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarHistorial(uid);
        return ResponseEntity.noContent().build();
    }

    // Elimina el historial de asesorías de un solicitante específico
    @DeleteMapping("/requester/{email}")
    public ResponseEntity<Void> eliminarHistorialSolicitante(
            @PathVariable("email") String email) {
        advisoryService.eliminarHistorialSolicitante(email);
        return ResponseEntity.noContent().build();
    }
}
