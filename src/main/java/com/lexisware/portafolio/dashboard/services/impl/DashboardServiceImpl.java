package com.lexisware.portafolio.dashboard.services.impl;

import com.lexisware.portafolio.dashboard.dtos.DashboardStats;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;
import com.lexisware.portafolio.dashboard.mappers.DashboardMapper;
import com.lexisware.portafolio.dashboard.models.DashboardSummary;
import com.lexisware.portafolio.dashboard.services.DashboardService;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Implementación del servicio de métricas para el dashboard
@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AdvisoryRepository advisoryRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(UserRepository userRepository, ProjectRepository projectRepository,
            AdvisoryRepository advisoryRepository, DashboardMapper dashboardMapper) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.advisoryRepository = advisoryRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStats getGlobalStats() {
        DashboardSummary summary = new DashboardSummary(
                userRepository.count(),
                projectRepository.count(),
                advisoryRepository.countByStatus(AdvisoryEntity.Status.pending),
                advisoryRepository.countByStatus(AdvisoryEntity.Status.approved),
                advisoryRepository.countByStatus(AdvisoryEntity.Status.rejected));

        return dashboardMapper.toStatsDto(summary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProjectCount> getProjectsByUser() {
        return projectRepository.countProjectsByUser();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGrowthStats> getUserGrowth() {
        return userRepository.countUsersByGrowth();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvisoryStatsDto> getAdvisoryHistory() {
        return advisoryRepository.countAdvisoriesByMonth();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvisoryStatsDto> getAdvisoriesByProgrammer() {
        return advisoryRepository.countAdvisoriesByProgrammer();
    }
}
