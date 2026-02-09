package com.lexisware.portafolio.users.repositories;

import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio de usuarios
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // Busca usuario por email
    Optional<UserEntity> findByEmail(String email);

    // Busca usuarios por rol
    List<UserEntity> findByRole(UserEntity.Role role);

    // Busca programadores disponibles
    List<UserEntity> findByAvailableTrue();

    // Busca programadores disponibles por rol
    List<UserEntity> findByRoleAndAvailableTrue(UserEntity.Role role);

    // Cuenta usuarios por rol
    long countByRole(UserEntity.Role role);

    // Estadísticas: Crecimiento usuarios/mes
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.UserGrowthStats(CAST(EXTRACT(MONTH FROM u.createdAt) AS int), CAST(EXTRACT(YEAR FROM u.createdAt) AS int), COUNT(u)) FROM UserEntity u GROUP BY EXTRACT(YEAR FROM u.createdAt), EXTRACT(MONTH FROM u.createdAt) ORDER BY EXTRACT(YEAR FROM u.createdAt), EXTRACT(MONTH FROM u.createdAt)")
    List<UserGrowthStats> countUsersByGrowth();
}
