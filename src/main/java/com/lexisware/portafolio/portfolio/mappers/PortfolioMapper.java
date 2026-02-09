package com.lexisware.portafolio.portfolio.mappers;

import com.lexisware.portafolio.portfolio.dtos.PortfolioRequestDto;
import com.lexisware.portafolio.portfolio.dtos.PortfolioResponseDto;
import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.portfolio.models.Portfolio;
import com.lexisware.portafolio.project.mappers.ProjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Mapper encargado de transformar datos de portafolio entre DTOs, modelos y entidades
@Component
public class PortfolioMapper {

    @Autowired
    @Lazy // Evita la dependencia circular con ProjectMapper durante la inicialización
    private ProjectMapper projectMapper;

    // Convierte una entidad JPA de persistencia a un modelo de negocio de
    // portafolio
    public Portfolio toModel(PortfolioEntity entity) {
        if (entity == null)
            return null;
        Portfolio model = new Portfolio();
        model.setId(entity.getId());
        model.setUserId(entity.getUserId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setTheme(entity.getTheme());
        model.setIsPublic(entity.getIsPublic());
        model.setSkills(entity.getSkills() != null ? new java.util.ArrayList<>(entity.getSkills())
                : new java.util.ArrayList<>());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());

        // Mapea recursivamente la lista de proyectos internos
        if (entity.getProjects() != null) {
            model.setProjects(projectMapper.toModelList(entity.getProjects()));
        } else {
            model.setProjects(Collections.emptyList());
        }

        return model;
    }

    // Transforma un modelo de negocio a una entidad JPA para su almacenamiento
    public PortfolioEntity toEntity(Portfolio model) {
        if (model == null)
            return null;
        PortfolioEntity entity = new PortfolioEntity();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setUserId(model.getUserId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setTheme(model.getTheme());
        entity.setIsPublic(model.getIsPublic());
        entity.setSkills(
                model.getSkills() != null ? new java.util.ArrayList<>(model.getSkills()) : new java.util.ArrayList<>());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    // Mapea los datos de una solicitud DTO a un modelo de negocio inicial
    public Portfolio toModel(PortfolioRequestDto dto) {
        if (dto == null)
            return null;
        Portfolio model = new Portfolio();
        model.setUserId(dto.getUserId());
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setTheme(dto.getTheme());
        model.setIsPublic(dto.getIsPublic());
        model.setSkills(
                dto.getSkills() != null ? new java.util.ArrayList<>(dto.getSkills()) : new java.util.ArrayList<>());
        return model;
    }

    // Actualiza el estado de un modelo existente a partir de los campos presentes
    // en el DTO
    public void updateModel(Portfolio model, PortfolioRequestDto dto) {
        if (dto.getTitle() != null)
            model.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            model.setDescription(dto.getDescription());
        if (dto.getTheme() != null)
            model.setTheme(dto.getTheme());
        if (dto.getIsPublic() != null)
            model.setIsPublic(dto.getIsPublic());
        if (dto.getSkills() != null)
            model.setSkills(dto.getSkills());
    }

    // Prepara un objeto DTO de respuesta para ser enviado a través de la API
    public PortfolioResponseDto toResponseDto(Portfolio model) {
        if (model == null)
            return null;
        PortfolioResponseDto dto = new PortfolioResponseDto();
        dto.setId(model.getId());
        dto.setUserId(model.getUserId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setTheme(model.getTheme());
        dto.setIsPublic(model.getIsPublic());
        dto.setSkills(
                model.getSkills() != null ? new java.util.ArrayList<>(model.getSkills()) : new java.util.ArrayList<>());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());

        // Mapea la lista de proyectos al formato de respuesta DTO
        if (model.getProjects() != null) {
            dto.setProjects(projectMapper.toResponseDtoList(model.getProjects()));
        } else {
            dto.setProjects(Collections.emptyList());
        }

        return dto;
    }

    // Convierte una colección de modelos en una lista de DTOs de respuesta
    public List<PortfolioResponseDto> toResponseDtoList(List<Portfolio> models) {
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Convierte una colección de entidades JPA en una lista de modelos de negocio
    public List<Portfolio> toModelList(List<PortfolioEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
