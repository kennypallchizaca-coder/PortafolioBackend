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

// Controlador de portafolios
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;

    public PortfolioController(PortfolioService portfolioService, PortfolioMapper portfolioMapper) {
        this.portfolioService = portfolioService;
        this.portfolioMapper = portfolioMapper;
    }

    // Obtiene portafolios públicos
    @GetMapping("/public")
    public ResponseEntity<List<PortfolioResponseDto>> obtenerPortafoliosPublicos() {
        List<Portfolio> portfolios = portfolioService.obtenerPortafoliosPublicos();
        return ResponseEntity.ok(portfolioMapper.toResponseDtoList(portfolios));
    }

    // Busca portafolio por ID
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorId(@PathVariable("id") Long id) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorId(id);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Obtiene mi portafolio
    @GetMapping("/me")
    public ResponseEntity<PortfolioResponseDto> obtenerMiPortafolio(@AuthenticationPrincipal String uid) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(uid);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Obtiene portafolio por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorUsuario(@PathVariable("userId") String userId) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(userId);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // Crea portafolio
    @PostMapping
    public ResponseEntity<PortfolioResponseDto> crearPortafolio(
            @AuthenticationPrincipal String uid,
            @Valid @RequestBody PortfolioRequestDto request) {
        // Asigna portafolio al usuario actual
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            request.setUserId(uid);
        }
        Portfolio portfolioModel = portfolioMapper.toModel(request);
        Portfolio created = portfolioService.crearPortafolio(portfolioModel);
        return new ResponseEntity<>(portfolioMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Actualiza portafolio
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

    // Elimina portafolio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPortafolio(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {

        portfolioService.eliminarPortafolio(id, uid);
        return ResponseEntity.noContent().build();
    }
}
