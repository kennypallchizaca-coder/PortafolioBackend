package com.lexisware.portafolio.portfolio.repositories;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

// Repositorio de portafolios
@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    // Busca portafolio por UID de usuario
    Optional<PortfolioEntity> findByUserId(String userId);

    // Lista portafolios públicos
    List<PortfolioEntity> findByIsPublicTrue();

    // Verifica existencia por usuario
    boolean existsByUserId(String userId);
}
