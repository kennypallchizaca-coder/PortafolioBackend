package com.lexisware.portafolio.files.mappers;

import com.lexisware.portafolio.files.entities.FileEntity;
import com.lexisware.portafolio.files.models.FileModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// Mapper para transformar entre la entidad de base de datos y el modelo de dominio de archivos
@Component
public class FileMapper {

    // Convierte una entidad JPA a modelo de dominio
    public FileModel toModel(FileEntity entity) {
        if (entity == null)
            return null;
        FileModel model = new FileModel();
        model.setId(entity.getId());
        model.setUrl(entity.getUrl());
        model.setPublicId(entity.getPublicId());
        model.setFolder(entity.getFolder());
        model.setCreatedAt(entity.getCreatedAt());
        return model;
    }

    // Convierte un modelo de dominio a entidad JPA
    public FileEntity toEntity(FileModel model) {
        if (model == null)
            return null;
        FileEntity entity = new FileEntity();
        entity.setId(model.getId());
        entity.setUrl(model.getUrl());
        entity.setPublicId(model.getPublicId());
        entity.setFolder(model.getFolder());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }

    // Transforma una lista de entidades a una lista de modelos
    public List<FileModel> toModelList(List<FileEntity> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}
