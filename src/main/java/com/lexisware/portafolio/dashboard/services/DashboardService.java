package com.lexisware.portafolio.dashboard.services;

import com.lexisware.portafolio.dashboard.dtos.DashboardStats;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;
import java.util.List;

// Interfaz para la obtención de métricas y estadísticas globales del dashboard
public interface DashboardService {

    // Obtiene estadísticas generales del sistema (usuarios, proyectos, asesorías)
    DashboardStats getGlobalStats();

    // Obtiene el ranking de proyectos por cada usuario
    List<UserProjectCount> getProjectsByUser();

    // Obtiene las estadísticas de crecimiento de usuarios
    List<UserGrowthStats> getUserGrowth();

    // Obtiene el historial de asesorías por mes
    List<AdvisoryStatsDto> getAdvisoryHistory();

    // Obtiene la distribución de asesorías por cada programador
    List<AdvisoryStatsDto> getAdvisoriesByProgrammer();
}
