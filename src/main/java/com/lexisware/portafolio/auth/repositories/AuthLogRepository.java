package com.lexisware.portafolio.auth.repositories;

import com.lexisware.portafolio.auth.entities.AuthLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Repositorio para la gestión del historial de autenticación
@Repository
public interface AuthLogRepository extends JpaRepository<AuthLogEntity, Long> {

    // Recupera los últimos logs de un usuario específico
    List<AuthLogEntity> findTop10ByEmailOrderByTimestampDesc(String email);
}
