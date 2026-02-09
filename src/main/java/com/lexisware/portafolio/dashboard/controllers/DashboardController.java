package com.lexisware.portafolio.dashboard.controllers;

import com.lexisware.portafolio.dashboard.dtos.DashboardStats;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;
import com.lexisware.portafolio.dashboard.services.DashboardService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para obtener métricas y estadísticas globales del sistema
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

        private final DashboardService dashboardService;

        // Inicializa el controlador con el servicio de dashboard
        public DashboardController(DashboardService dashboardService) {
                this.dashboardService = dashboardService;
        }

        // Retorna un resumen general: cantidad de usuarios, proyectos y estados de
        // asesorías
        @GetMapping("/stats")
        public ResponseEntity<DashboardStats> getStats() {
                return ResponseEntity.ok(dashboardService.getGlobalStats());
        }

        // Obtiene el ranking de cantidad de proyectos por cada usuario
        @GetMapping("/projects-by-user")
        public ResponseEntity<List<UserProjectCount>> getProjectStatsByUser() {
                return ResponseEntity.ok(dashboardService.getProjectsByUser());
        }

        // Retorna las estadísticas de crecimiento de usuarios registrados a lo largo
        // del tiempo
        @GetMapping("/user-growth")
        public ResponseEntity<List<UserGrowthStats>> getUserGrowth() {
                return ResponseEntity.ok(dashboardService.getUserGrowth());
        }

        // Obtiene el historial de asesorías solicitado agrupado por mes
        @GetMapping("/advisories-history")
        public ResponseEntity<List<AdvisoryStatsDto>> getAdvisoryHistory() {
                return ResponseEntity.ok(dashboardService.getAdvisoryHistory());
        }

        // Retorna la distribución de asesorías atendidas por cada programador
        @GetMapping("/advisories-by-programmer")
        public ResponseEntity<List<AdvisoryStatsDto>> getAdvisoriesByProgrammer() {
                return ResponseEntity.ok(dashboardService.getAdvisoriesByProgrammer());
        }
}
