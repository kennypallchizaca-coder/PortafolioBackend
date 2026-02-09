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

// Servicio simplificado para gestión de usuarios
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtiene listado de programadores
    @Override
    @Transactional(readOnly = true)
    public List<User> obtenerProgramadores() {
        return userMapper.toModelList(userRepository.findByRole(UserEntity.Role.PROGRAMMER));
    }

    // Obtiene programadores disponibles
    @Override
    @Transactional(readOnly = true)
    public List<User> obtenerProgramadoresDisponibles() {
        return userMapper.toModelList(userRepository.findByRole(UserEntity.Role.PROGRAMMER));
    }

    // Busca usuario por UID
    @Override
    @Transactional(readOnly = true)
    public User obtenerUsuarioPorId(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        return userMapper.toModel(entity);
    }

    // Crea o actualiza usuario (Lógica centralizada)
    @Override
    @Transactional
    public User crearOActualizarUsuario(User userModel) {
        // Flujo: Buscar -> Preservar datos valiosos -> Guardar
        userRepository.findById(userModel.getUid()).ifPresentOrElse(
                existing -> {
                    // Mantener contraseña si no viene nueva o es igual a la actual (ya encriptada)
                    if (userModel.getPassword() != null && !userModel.getPassword().isEmpty()
                            && !userModel.getPassword().equals(existing.getPassword())) {
                        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    } else {
                        userModel.setPassword(existing.getPassword());
                    }

                    // Mantener fecha de creación
                    if (userModel.getCreatedAt() == null) {
                        userModel.setCreatedAt(existing.getCreatedAt());
                    }

                    // Mantener foto si la nueva es vacía (prevención de borrado accidental)
                    if (userModel.getPhotoURL() == null || userModel.getPhotoURL().trim().isEmpty()) {
                        userModel.setPhotoURL(existing.getPhotoURL());
                    }

                    userModel.setUpdatedAt(LocalDateTime.now());
                },
                () -> {
                    // Nuevo usuario: Asignar clave por defecto si falta
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

    // NOTA: El método 'actualizarUsuario' (parcial) se eliminó por no ser utlizado.
    // El controlador maneja la actualización parcial mapeando el DTO al modelo y
    // llamando a crearOActualizarUsuario.

    // Actualiza disponibilidad
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

    // Elimina usuario por ID
    @Override
    @Transactional
    public void eliminarUsuario(String uid) {
        if (!userRepository.existsById(uid)) {
            throw new ResourceNotFoundException("Usuario", "uid", uid);
        }
        userRepository.deleteById(uid);
    }
}
