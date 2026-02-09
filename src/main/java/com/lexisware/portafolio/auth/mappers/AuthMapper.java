package com.lexisware.portafolio.auth.mappers;

import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.models.UserSession;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// Mapper encargado de transformar datos de sesión entre modelos y respuestas del API
@Component
public class AuthMapper {

    // Transforma un modelo de sesión interna a una respuesta DTO de autenticación
    public AuthResponse toAuthResponse(UserSession session, AuthResponse.UserDTO userDto) {
        if (session == null)
            return null;
        return new AuthResponse(session.getToken(), userDto);
    }

    // Crea una instancia de sesión a partir de datos básicos
    public UserSession createSession(String uid, String email, String token, long expirationMs) {
        LocalDateTime now = LocalDateTime.now();
        return new UserSession(uid, email, token, now, now.plusNanos(expirationMs * 1_000_000));
    }
}
