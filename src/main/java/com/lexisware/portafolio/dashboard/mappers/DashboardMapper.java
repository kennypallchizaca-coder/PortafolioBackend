package com.lexisware.portafolio.dashboard.mappers;

import com.lexisware.portafolio.dashboard.dtos.*;
import com.lexisware.portafolio.dashboard.models.*;
import org.springframework.stereotype.Component;

// Mapper para desacoplar modelos de dominio de las respuestas del API en el dashboard
@Component
public class DashboardMapper {

    // Transforma métricas de dominio a respuesta DTO
    public DashboardStats toStatsDto(DashboardSummary summary) {
        if (summary == null)
            return null;
        return DashboardStats.builder()
                .programmersCount(summary.getProgrammersCount())
                .projectsCount(summary.getProjectsCount())
                .advisoriesPending(summary.getAdvisoriesPending())
                .advisoriesApproved(summary.getAdvisoriesApproved())
                .advisoriesRejected(summary.getAdvisoriesRejected())
                .build();
    }
}
