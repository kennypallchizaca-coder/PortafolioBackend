package com.lexisware.portafolio.project.controllers;

import com.lexisware.portafolio.project.services.ProjectService;
import com.lexisware.portafolio.project.mappers.ProjectMapper;
import com.lexisware.portafolio.project.dtos.ProjectRequestDto;
import com.lexisware.portafolio.project.dtos.ProjectResponseDto;
import com.lexisware.portafolio.project.models.Project;
import com.lexisware.portafolio.project.entities.ProjectEntity;

import com.lexisware.portafolio.portfolio.services.PortfolioService;
import com.lexisware.portafolio.users.services.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

// Controlador de proyectos
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final PortfolioService portfolioService;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper, UserService userService,
            PortfolioService portfolioService) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    // Obtiene todos los proyectos paginados
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> obtenerTodosLosProyectos(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerTodosLosProyectos(pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Busca proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> obtenerProyectoPorId(@PathVariable("id") Long id) {
        Project project = projectService.obtenerProyectoPorId(id);
        return ResponseEntity.ok(projectMapper.toResponseDto(project));
    }

    // Obtiene mis proyectos
    @GetMapping("/my-projects")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerMisProyectos(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorPropietario(uid, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Obtiene proyectos por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorUsuario(
            @PathVariable("userId") String userId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorPropietario(userId, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Filtra proyectos por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorCategoria(
            @PathVariable("category") ProjectEntity.Category category,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorCategoria(category, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Filtra proyectos por rol
    @GetMapping("/role/{role}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorRol(
            @PathVariable("role") ProjectEntity.ProjectRole role,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorRol(role, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Crea nuevo proyecto
    @PostMapping
    public ResponseEntity<ProjectResponseDto> crearProyecto(
            @Valid @RequestBody ProjectRequestDto request,
            @AuthenticationPrincipal String uid) {

        Project projectModel = projectMapper.toModel(request);

        // Usuario actual como propietario
        String ownerUid = uid;

        // Vincula usuario complet
        try {
            projectModel.setOwner(userService.obtenerUsuarioPorId(ownerUid));
        } catch (Exception e) {
            log.error("Error al obtener usuario para vincular al proyecto: {}", e.getMessage());
        }

        // Vincula a portafolio si existeID
        if (request.getPortfolioId() != null) {
            try {
                projectModel.setPortfolio(portfolioService.obtenerPortafolioPorId(request.getPortfolioId()));
            } catch (Exception e) {
                log.error("Error al obtener portafolio para vincular al proyecto: {}", e.getMessage());
            }
        }

        Project created = projectService.crearProyecto(projectModel);
        return new ResponseEntity<>(projectMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Actualiza proyecto
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> actualizarProyecto(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProjectRequestDto request,
            @AuthenticationPrincipal String uid) {

        Project existingModel = projectService.obtenerProyectoPorId(id);
        projectMapper.updateModel(existingModel, request);

        Project updated = projectService.actualizarProyecto(id, existingModel, uid);

        return ResponseEntity.ok(projectMapper.toResponseDto(updated));
    }

    // Elimina proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {

        projectService.eliminarProyecto(id, uid);
        return ResponseEntity.noContent().build();
    }
}
