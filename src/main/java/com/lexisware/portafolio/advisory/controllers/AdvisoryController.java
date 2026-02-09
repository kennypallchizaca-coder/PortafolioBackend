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

// Controlador REST de asesorías
@RestController
@RequestMapping("/api/advisories")
public class AdvisoryController {

    private final AdvisoryService advisoryService;
    private final AdvisoryMapper advisoryMapper;

    public AdvisoryController(AdvisoryService advisoryService, AdvisoryMapper advisoryMapper) {
        this.advisoryService = advisoryService;
        this.advisoryMapper = advisoryMapper;
    }

    // Obtiene todas las asesorías (Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerTodasLasAsesorias(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerTodasLasAsesorias(pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene asesoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<AdvisoryResponseDto> obtenerAsesoriaPorId(@PathVariable("id") Long id) {
        Advisory advisory = advisoryService.obtenerAsesoriaPorId(id);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(advisory));
    }

    // Obtiene mis asesorías asignadas
    @GetMapping("/my-advisories")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerMisAsesorias(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(uid, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene asesorías por programador
    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorProgramador(
            @PathVariable("programmerId") String programmerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(programmerId, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Obtiene asesorías por email solicitante
    @GetMapping("/requester/{email}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorSolicitante(
            @PathVariable("email") String email,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorSolicitante(email, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Filtra asesorías por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorEstado(
            @PathVariable("status") AdvisoryEntity.Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorEstado(status, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Crea nueva asesoría
    @PostMapping
    public ResponseEntity<AdvisoryResponseDto> crearAsesoria(@Valid @RequestBody AdvisoryRequestDto request) {
        Advisory advisoryModel = advisoryMapper.toModel(request);
        Advisory created = advisoryService.crearAsesoria(advisoryModel);
        return new ResponseEntity<>(advisoryMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Actualiza estado de asesoría
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

    // Aprueba asesoría
    @PatchMapping("/{id}/approve")
    public ResponseEntity<AdvisoryResponseDto> aprobarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory approved = advisoryService.aprobarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(approved));
    }

    // Rechaza asesoría
    @PatchMapping("/{id}/reject")
    public ResponseEntity<AdvisoryResponseDto> rechazarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory rejected = advisoryService.rechazarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(rejected));
    }

    // Elimina asesoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarAsesoria(id, uid);
        return ResponseEntity.noContent().build();
    }

    // Elimina historial de asesorías (completadas)
    @DeleteMapping("/history")
    public ResponseEntity<Void> eliminarHistorial(
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarHistorial(uid);
        return ResponseEntity.noContent().build();
    }

    // Elimina historial de asesorías de solicitante (completadas)
    @DeleteMapping("/requester/{email}")
    public ResponseEntity<Void> eliminarHistorialSolicitante(
            @PathVariable("email") String email) {
        advisoryService.eliminarHistorialSolicitante(email);
        return ResponseEntity.noContent().build();
    }
}
