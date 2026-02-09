package com.lexisware.portafolio.dashboard.repositories;

import com.lexisware.portafolio.dashboard.entities.DashboardConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para la persistencia de configuraciones de dashboard por usuario
@Repository
public interface DashboardConfigRepository extends JpaRepository<DashboardConfigEntity, String> {
}
