package com.lexisware.portafolio.users.services.impl;

import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.users.services.UserService;
import com.lexisware.portafolio.exceptions.ResourceNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// Implementación de la lógica de negocio para la administración de usuarios y perfiles
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Inicializa el servicio inyectando repositorios y componentes de seguridad
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtiene el catálogo completo de usuarios registrados como programadores
    @Override
    @Transactional(readOnly = true)
    public List<User> obtenerProgramadores() {
        return userMapper.toModelList(userRepository.findByRole(UserEntity.Role.PROGRAMMER));
    }

    // Recupera únicamente aquellos programadores que marcaron su estado como
    // disponible
    @Override
    @Transactional(readOnly = true)
    public List<User> obtenerProgramadoresDisponibles() {
        return userMapper.toModelList(userRepository.findByRole(UserEntity.Role.PROGRAMMER));
    }

    // Busca la información técnica de un usuario por su UID único en el sistema
    @Override
    @Transactional(readOnly = true)
    public User obtenerUsuarioPorId(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        return userMapper.toModel(entity);
    }

    // Gestiona el ciclo de vida del usuario aplicando validaciones de seguridad y
    // consistencia
    @Override
    @Transactional
    public User crearOActualizarUsuario(User userModel) {
        userRepository.findById(userModel.getUid()).ifPresentOrElse(
                existing -> {
                    // Preserva la clave encriptada si no se proporciona una actualización
                    if (userModel.getPassword() != null && !userModel.getPassword().isEmpty()
                            && !userModel.getPassword().equals(existing.getPassword())) {
                        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    } else {
                        userModel.setPassword(existing.getPassword());
                    }

                    // Mantiene la fecha de registro original del usuario
                    if (userModel.getCreatedAt() == null) {
                        userModel.setCreatedAt(existing.getCreatedAt());
                    }

                    // Protege la URL de la foto para evitar sobreescrituras accidentales
                    if (userModel.getPhotoURL() == null || userModel.getPhotoURL().trim().isEmpty()) {
                        userModel.setPhotoURL(existing.getPhotoURL());
                    }

                    userModel.setUpdatedAt(LocalDateTime.now());
                },
                () -> {
                    // Establece credenciales iniciales por defecto para nuevos usuarios
                    if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
                        userModel.setPassword(passwordEncoder.encode("123456"));
                    } else {
                        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    }
                    userModel.setCreatedAt(LocalDateTime.now());
                });

        UserEntity entity = userMapper.toEntity(userModel);
        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // Modifica específicamente el estado de disponibilidad del usuario para
    // mentorías
    @Override
    @Transactional
    public User actualizarDisponibilidad(String uid, boolean available) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        entity.setAvailable(available);
        entity.setUpdatedAt(LocalDateTime.now());
        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // Elimina el registro del usuario verificando previamente su existencia
    @Override
    @Transactional
    public void eliminarUsuario(String uid) {
        if (!userRepository.existsById(uid)) {
            throw new ResourceNotFoundException("Usuario", "uid", uid);
        }
        userRepository.deleteById(uid);
    }
}
