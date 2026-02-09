package com.lexisware.portafolio.portfolio.repositories;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

// Repositorio para operaciones de persistencia JPA sobre la entidad PortfolioEntity
@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    // Recupera un portafolio único asociado al identificador UID de un usuario
    Optional<PortfolioEntity> findByUserId(String userId);

    // Obtiene todos los portafolios que tienen activada la bandera de visibilidad
    // pública
    List<PortfolioEntity> findByIsPublicTrue();

    // Comprueba si ya existe un portafolio registrado para un UID de usuario
    // específico
    boolean existsByUserId(String userId);
}
