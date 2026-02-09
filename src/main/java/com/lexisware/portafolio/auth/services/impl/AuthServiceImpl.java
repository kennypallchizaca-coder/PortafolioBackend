package com.lexisware.portafolio.auth.services.impl;

import com.lexisware.portafolio.utils.EmailService;
import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.dtos.LoginRequest;
import com.lexisware.portafolio.auth.dtos.RegisterRequest;
import com.lexisware.portafolio.auth.services.AuthService;
import com.lexisware.portafolio.auth.mappers.AuthMapper;
import com.lexisware.portafolio.auth.models.UserSession;
import com.lexisware.portafolio.auth.entities.AuthLogEntity;
import com.lexisware.portafolio.auth.repositories.AuthLogRepository;
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

// Implementación del servicio de autenticación con auditoría integrada
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final AuthLogRepository authLogRepository;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider, EmailService emailService, UserMapper userMapper,
            AuthMapper authMapper, AuthLogRepository authLogRepository,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.authMapper = authMapper;
        this.authLogRepository = authLogRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        log.info("Registrando usuario: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        UserEntity user = new UserEntity();
        user.setUid(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());

        String roleStr = (request.getRole() == null || request.getRole().isBlank()) ? "EXTERNAL"
                : request.getRole().toUpperCase();
        user.setRole(UserEntity.Role.valueOf(roleStr));
        user.setAvailable(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user = userRepository.save(user);

        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getDisplayName());
        } catch (Exception e) {
            log.error("Error enviando email de bienvenida: {}", e.getMessage());
        }

        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        // Registro de auditoría
        authLogRepository.save(new AuthLogEntity(user.getEmail(), true, "local-registration"));

        // Crea sesión interna
        UserSession session = authMapper.createSession(user.getUid(), user.getEmail(), token, 86400000L);
        log.info("Sesión iniciada para el registro: {}", session.getEmail());

        return authMapper.toAuthResponse(session, AuthResponse.UserDTO.fromEntity(user));
    }

    @Override
    public AuthResponse iniciarSesion(LoginRequest request) {
        log.info("Iniciando sesión: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado (inconsistencia)"));

            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            String token = jwtTokenProvider.generarToken(
                    user.getUid(),
                    user.getEmail(),
                    user.getRole().name());

            // Registro de auditoría exitosa
            authLogRepository.save(new AuthLogEntity(request.getEmail(), true, "api-login"));

            // Crea sesión interna
            UserSession session = authMapper.createSession(user.getUid(), user.getEmail(), token, 86400000L);
            log.info("Login exitoso, sesión creada: {}", session.getEmail());

            return authMapper.toAuthResponse(session, AuthResponse.UserDTO.fromEntity(user));

        } catch (Exception e) {
            // Registro de auditoría fallida
            authLogRepository.save(new AuthLogEntity(request.getEmail(), false, "failed-login-attempt"));
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }

    @Override
    public User obtenerUsuarioActual(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toModel(entity);
    }
}
