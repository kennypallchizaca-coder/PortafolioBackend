package com.lexisware.portafolio.users.repositories;

import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Interfaz para la manipulación persistente de usuarios mediante Spring Data JPA
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // Localiza un usuario por su dirección de correo electrónico única
    Optional<UserEntity> findByEmail(String email);

    // Lista todos los usuarios que poseen un rol de acceso específico
    List<UserEntity> findByRole(UserEntity.Role role);

    // Recupera programadores que han activado su estado de disponibilidad
    List<UserEntity> findByAvailableTrue();

    // Filtra usuarios disponibles que además coinciden con un rol específico
    List<UserEntity> findByRoleAndAvailableTrue(UserEntity.Role role);

    // Retorna la cantidad total de usuarios registrados bajo un rol determinado
    long countByRole(UserEntity.Role role);

    // Consulta personalizada para obtener estadísticas de registro mensual de
    // usuarios
    @Query("SELECT new com.lexisware.portafolio.dashboard.dtos.UserGrowthStats(CAST(EXTRACT(MONTH FROM u.createdAt) AS int), CAST(EXTRACT(YEAR FROM u.createdAt) AS int), COUNT(u)) FROM UserEntity u GROUP BY EXTRACT(YEAR FROM u.createdAt), EXTRACT(MONTH FROM u.createdAt) ORDER BY EXTRACT(YEAR FROM u.createdAt), EXTRACT(MONTH FROM u.createdAt)")
    List<UserGrowthStats> countUsersByGrowth();
}
