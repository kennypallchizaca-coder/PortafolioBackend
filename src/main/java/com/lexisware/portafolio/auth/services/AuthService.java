package com.lexisware.portafolio.auth.services;

import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.dtos.LoginRequest;
import com.lexisware.portafolio.auth.dtos.RegisterRequest;
import com.lexisware.portafolio.users.models.User;

// Interfaz para la lógica de autenticación y gestión de sesiones
public interface AuthService {

    // Registra un nuevo usuario en el sistema
    AuthResponse registrar(RegisterRequest request);

    // Inicia sesión y devuelve un token de acceso
    AuthResponse iniciarSesion(LoginRequest request);

    // Obtiene la información del usuario actual mediante su UID
    User obtenerUsuarioActual(String uid);
}
