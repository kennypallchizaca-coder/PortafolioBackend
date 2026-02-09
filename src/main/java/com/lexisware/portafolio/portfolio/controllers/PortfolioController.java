package com.lexisware.portafolio.portfolio.controllers;

import com.lexisware.portafolio.portfolio.services.PortfolioService;
import com.lexisware.portafolio.portfolio.mappers.PortfolioMapper;
import com.lexisware.portafolio.portfolio.dtos.PortfolioRequestDto;
import com.lexisware.portafolio.portfolio.dtos.PortfolioResponseDto;
import com.lexisware.portafolio.portfolio.models.Portfolio;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// API para la gestión de portafolios profesionales de los usuarios
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;

    // Inicializa el controlador inyectando el servicio de negocio y el mapeador de
    // DTOs
    public PortfolioController(PortfolioService portfolioService, PortfolioMapper portfolioMapper) {
        this.portfolioService = portfolioService;
        this.portfolioMapper = portfolioMapper;
    }

    // Retorna una lista de todos los portafolios marcados como públicos
    @GetMapping("/public")
    public ResponseEntity<List<PortfolioResponseDto>> obtenerPortafoliosPublicos() {
        List<Portfolio> portfolios = portfolioService.obtenerPortafoliosPublicos();
        return ResponseEntity.ok(portfolioMapper.toResponseDtoList(portfolios));
    }

    // Busca un portafolio específico por su identificador numérico único
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorId(@PathVariable("id") Long id) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorId(id);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Recupera la información del portafolio del usuario autenticado actualmente
    @GetMapping("/me")
    public ResponseEntity<PortfolioResponseDto> obtenerMiPortafolio(@AuthenticationPrincipal String uid) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(uid);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Permite consultar el portafolio de cualquier usuario mediante su
    // identificador UID
    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorUsuario(@PathVariable("userId") String userId) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(userId);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Crea un nuevo registro de portafolio para el usuario autenticado
    @PostMapping
    public ResponseEntity<PortfolioResponseDto> crearPortafolio(
            @AuthenticationPrincipal String uid,
            @Valid @RequestBody PortfolioRequestDto request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            request.setUserId(uid);
        }
        Portfolio portfolioModel = portfolioMapper.toModel(request);
        Portfolio created = portfolioService.crearPortafolio(portfolioModel);
        return new ResponseEntity<>(portfolioMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Actualiza parcialmente la información de un portafolio existente
    @PatchMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> actualizarPortafolio(
            @PathVariable("id") Long id,
            @Valid @RequestBody PortfolioRequestDto request,
            @AuthenticationPrincipal String uid) {

        Portfolio existingModel = portfolioService.obtenerPortafolioPorId(id);
        portfolioMapper.updateModel(existingModel, request);

        Portfolio updated = portfolioService.actualizarPortafolio(id, existingModel, uid);

        return ResponseEntity.ok(portfolioMapper.toResponseDto(updated));
    }

    // Elimina de forma permanente un portafolio validando la propiedad del mismo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPortafolio(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {

        portfolioService.eliminarPortafolio(id, uid);
        return ResponseEntity.noContent().build();
    }
}
