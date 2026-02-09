package com.lexisware.portafolio.users.mappers;

import com.lexisware.portafolio.users.dtos.UserResponseDto;
import com.lexisware.portafolio.users.dtos.UserUpdateRequestDto;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Componente para la transformación de datos entre Entidades JPA, Modelos de Negocio y DTOs de Usuario
@Component
public class UserMapper {

    // Transforma una entidad de persistencia a un modelo de dominio de negocio
    public User toModel(UserEntity entity) {
        if (entity == null)
            return null;

        User model = new User();
        model.setUid(entity.getUid());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setDisplayName(entity.getDisplayName());

        // Asegura la compatibilidad de tipos entre enums de roles
        if (entity.getRole() != null) {
            model.setRole(User.Role.valueOf(entity.getRole().name()));
        }

        model.setSpecialty(entity.getSpecialty());
        model.setBio(entity.getBio());
        model.setPhotoURL(entity.getPhotoURL());
        model.setAvailable(entity.getAvailable());
        model.setSkills(entity.getSkills() != null ? new ArrayList<>(entity.getSkills()) : new ArrayList<>());
        model.setSchedule(entity.getSchedule() != null ? new ArrayList<>(entity.getSchedule()) : new ArrayList<>());
        model.setGithub(entity.getGithub());
        model.setInstagram(entity.getInstagram());
        model.setWhatsapp(entity.getWhatsapp());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    // Convierte un modelo de dominio en una entidad persistible para la base de
    // datos
    public UserEntity toEntity(User model) {
        if (model == null)
            return null;

        UserEntity entity = new UserEntity();
        entity.setUid(model.getUid());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setDisplayName(model.getDisplayName());

        // Mapea el rol del modelo al formato enumerado de la entidad JPA
        if (model.getRole() != null) {
            entity.setRole(UserEntity.Role.valueOf(model.getRole().name()));
        }

        entity.setSpecialty(model.getSpecialty());
        entity.setBio(model.getBio());
        entity.setPhotoURL(model.getPhotoURL());
        entity.setAvailable(model.getAvailable());
        entity.setSkills(model.getSkills() != null ? new ArrayList<>(model.getSkills()) : new ArrayList<>());
        entity.setSchedule(model.getSchedule() != null ? new ArrayList<>(model.getSchedule()) : new ArrayList<>());
        entity.setGithub(model.getGithub());
        entity.setInstagram(model.getInstagram());
        entity.setWhatsapp(model.getWhatsapp());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    // Sincroniza los cambios parciales enviados en la petición con el modelo de
    // usuario actual
    public void updateModel(User user, UserUpdateRequestDto dto) {
        if (dto.getDisplayName() != null) {
            user.setDisplayName(dto.getDisplayName());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }
        if (dto.getSpecialty() != null) {
            user.setSpecialty(dto.getSpecialty());
        }
        if (dto.getPhotoURL() != null) {
            user.setPhotoURL(dto.getPhotoURL());
        }
        if (dto.getSkills() != null) {
            user.setSkills(dto.getSkills());
        }
        if (dto.getSchedule() != null) {
            user.setSchedule(dto.getSchedule());
        }
        if (dto.getAvailable() != null) {
            user.setAvailable(dto.getAvailable());
        }
        // Actualiza individualmente los enlaces a perfiles externos configurados
        if (dto.getGithub() != null) {
            user.setGithub(dto.getGithub());
        }
        if (dto.getInstagram() != null) {
            user.setInstagram(dto.getInstagram());
        }
        if (dto.getWhatsapp() != null) {
            user.setWhatsapp(dto.getWhatsapp());
        }
    }

    // Transforma el modelo de dominio a una representación simplificada para
    // respuestas de API
    public UserResponseDto toResponseDto(User user) {
        if (user == null)
            return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setUid(user.getUid());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getDisplayName());

        // Asegura que el rol se transmita correctamente en la respuesta serializada
        if (user.getRole() != null) {
            dto.setRole(com.lexisware.portafolio.users.entities.UserEntity.Role.valueOf(user.getRole().name()));
        }

        dto.setSpecialty(user.getSpecialty());
        dto.setBio(user.getBio());
        dto.setPhotoURL(user.getPhotoURL());
        dto.setAvailable(user.getAvailable());
        dto.setSkills(user.getSkills() != null ? new ArrayList<>(user.getSkills()) : new ArrayList<>());
        dto.setSchedule(user.getSchedule() != null ? new ArrayList<>(user.getSchedule()) : new ArrayList<>());
        dto.setGithub(user.getGithub());
        dto.setInstagram(user.getInstagram());
        dto.setWhatsapp(user.getWhatsapp());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    // Procesa una colección completa de modelos para su conversión a DTOs de
    // respuesta
    public List<UserResponseDto> toResponseDtoList(List<User> users) {
        return users.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Facilita la carga masiva de entidades transformándolas a modelos de negocio
    public List<User> toModelList(List<UserEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
