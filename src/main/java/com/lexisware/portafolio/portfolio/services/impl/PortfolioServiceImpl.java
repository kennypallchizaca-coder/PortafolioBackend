package com.lexisware.portafolio.portfolio.services.impl;

import com.lexisware.portafolio.portfolio.repositories.PortfolioRepository;
import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.portfolio.mappers.PortfolioMapper;
import com.lexisware.portafolio.portfolio.models.Portfolio;
import com.lexisware.portafolio.portfolio.services.PortfolioService;
import com.lexisware.portafolio.exceptions.ResourceNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// Implementación de la lógica de negocio para la administración de portafolios
@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    // Inyecta las dependencias necesarias para la persistencia y transformación de
    // datos
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    // Obtiene la lista de portafolios disponibles para visibilidad pública general
    @Override
    public List<Portfolio> obtenerPortafoliosPublicos() {
        return portfolioRepository.findByIsPublicTrue().stream()
                .map(portfolioMapper::toModel)
                .toList();
    }

    // Busca un portafolio por su ID y lanza excepción si no se encuentra
    @Override
    public Portfolio obtenerPortafolioPorId(Long id) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));
        return portfolioMapper.toModel(entity);
    }

    // Recupera el portafolio vinculado a un UID de usuario específico
    @Override
    public Portfolio obtenerPortafolioPorUsuario(String uid) {
        PortfolioEntity entity = portfolioRepository.findByUserId(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "usuario", uid));
        return portfolioMapper.toModel(entity);
    }

    // Crea un portafolio nuevo asegurando que el usuario solo posea uno en el
    // sistema
    @Override
    @Transactional
    public Portfolio crearPortafolio(Portfolio portfolioModel) {
        if (portfolioRepository.existsByUserId(portfolioModel.getUserId())) {
            throw new IllegalArgumentException("El usuario ya tiene un portafolio creado.");
        }

        PortfolioEntity entity = portfolioMapper.toEntity(portfolioModel);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        PortfolioEntity saved = portfolioRepository.save(entity);
        return portfolioMapper.toModel(saved);
    }

    // Valida que el usuario solicitante sea el propietario del portafolio o un
    // administrador
    private void validarPropiedad(PortfolioEntity portfolio, String appUserUid) {
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }

        if (portfolio.getUserId() != null && portfolio.getUserId().equals(appUserUid)) {
            return;
        }

        throw new AccessDeniedException("No tienes permisos para modificar este portafolio");
    }

    // Actualiza la información del portafolio tras verificar la autorización del
    // usuario
    @Override
    @Transactional
    public Portfolio actualizarPortafolio(Long id, Portfolio portfolioUpdateModel, String requestUserUid) {
        PortfolioEntity existingEntity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        validarPropiedad(existingEntity, requestUserUid);

        if (portfolioUpdateModel.getTitle() != null)
            existingEntity.setTitle(portfolioUpdateModel.getTitle());
        if (portfolioUpdateModel.getDescription() != null)
            existingEntity.setDescription(portfolioUpdateModel.getDescription());
        if (portfolioUpdateModel.getTheme() != null)
            existingEntity.setTheme(portfolioUpdateModel.getTheme());
        if (portfolioUpdateModel.getIsPublic() != null)
            existingEntity.setIsPublic(portfolioUpdateModel.getIsPublic());
        if (portfolioUpdateModel.getSkills() != null)
            existingEntity.setSkills(portfolioUpdateModel.getSkills());

        existingEntity.setUpdatedAt(LocalDateTime.now());

        PortfolioEntity saved = portfolioRepository.save(existingEntity);
        return portfolioMapper.toModel(saved);
    }

    // Elimina de forma definitiva un portafolio previa validación de permisos
    @Override
    @Transactional
    public void eliminarPortafolio(Long id, String requestUserUid) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        validarPropiedad(entity, requestUserUid);

        portfolioRepository.delete(entity);
    }
}
