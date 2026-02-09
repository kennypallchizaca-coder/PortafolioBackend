package com.lexisware.portafolio.auth.services;

import com.lexisware.portafolio.utils.EmailService;
import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.dtos.LoginRequest;
import com.lexisware.portafolio.auth.dtos.RegisterRequest;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.exceptions.ResourceNotFoundException;
import com.lexisware.portafolio.exceptions.UnauthorizedException;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.config.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// Servicio de autenticación y sesiones
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider, EmailService emailService, UserMapper userMapper,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    // Registra nuevo usuario
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        log.info("Registrando usuario: {}", request.getEmail());

        // Valida email único
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Mapea a entidad
        UserEntity user = new UserEntity();
        user.setUid(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encripta clave
        user.setDisplayName(request.getDisplayName());

        // Asigna rol (default EXTERNAL)
        String roleStr = (request.getRole() == null || request.getRole().isBlank()) ? "EXTERNAL"
                : request.getRole().toUpperCase();
        user.setRole(UserEntity.Role.valueOf(roleStr));
        user.setAvailable(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Guarda usuario
        user = userRepository.save(user);

        // Envía email de bienvenida
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getDisplayName());
        } catch (Exception e) {
            log.error("Error enviando email de bienvenida: {}", e.getMessage());
        }

        // Genera token JWT
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Usuario registrado exitosamente: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // Inicia sesión
    public AuthResponse iniciarSesion(LoginRequest request) {
        log.info("Iniciando sesión: {}", request.getEmail());

        // Autentica con Spring Security
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // Busca usuario completo
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado (inconsistencia)"));

        // Actualiza última actividad
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Genera token
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Login exitoso: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // Obtiene usuario actual
    public User obtenerUsuarioActual(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toModel(entity);
    }
}
