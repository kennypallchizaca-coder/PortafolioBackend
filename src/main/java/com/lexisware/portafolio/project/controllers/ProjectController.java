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

// API para la gestión integral de proyectos tecnológicos y portafolios
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final PortfolioService portfolioService;

    // Inicializa el controlador con servicios de proyectos, usuarios y portafolios
    public ProjectController(ProjectService projectService, ProjectMapper projectMapper, UserService userService,
            PortfolioService portfolioService) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    // Retorna una página de proyectos registrados en el sistema
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> obtenerTodosLosProyectos(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerTodosLosProyectos(pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Busca la información detallada de un proyecto por su identificadorID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> obtenerProyectoPorId(@PathVariable("id") Long id) {
        Project project = projectService.obtenerProyectoPorId(id);
        return ResponseEntity.ok(projectMapper.toResponseDto(project));
    }

    // Lista los proyectos asociados al usuario autenticado mediante paginación
    @GetMapping("/my-projects")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerMisProyectos(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorPropietario(uid, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Consulta los proyectos de un usuario específico mediante su identificador UID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorUsuario(
            @PathVariable("userId") String userId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorPropietario(userId, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Filtra el catálogo de proyectos por su categoría (académico o laboral)
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorCategoria(
            @PathVariable("category") ProjectEntity.Category category,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorCategoria(category, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Filtra proyectos según el rol desempeñado (frontend, backend, etc.)
    @GetMapping("/role/{role}")
    public ResponseEntity<Page<ProjectResponseDto>> obtenerProyectosPorRol(
            @PathVariable("role") ProjectEntity.ProjectRole role,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Project> projects = projectService.obtenerProyectosPorRol(role, pageable);
        return ResponseEntity.ok(projects.map(projectMapper::toResponseDto));
    }

    // Registra un nuevo proyecto vinculándolo al usuario actual y opcionalmente a
    // un portafolio
    @PostMapping
    public ResponseEntity<ProjectResponseDto> crearProyecto(
            @Valid @RequestBody ProjectRequestDto request,
            @AuthenticationPrincipal String uid) {

        Project projectModel = projectMapper.toModel(request);

        // Intenta asociar el objeto de usuario completo del propietario
        try {
            projectModel.setOwner(userService.obtenerUsuarioPorId(uid));
        } catch (Exception e) {
            log.error("Error al obtener usuario para vincular al proyecto: {}", e.getMessage());
        }

        // Vincula el proyecto a un portafolio específico si se proporciona el ID
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

    // Actualiza los datos de un proyecto existente validando la propiedad por UID
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

    // Elimina un proyecto del sistema validando los permisos del usuario
    // solicitante
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {

        projectService.eliminarProyecto(id, uid);
        return ResponseEntity.noContent().build();
    }
}
