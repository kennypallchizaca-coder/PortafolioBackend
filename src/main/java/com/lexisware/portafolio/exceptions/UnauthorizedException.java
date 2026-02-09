package com.lexisware.portafolio.exceptions;

// Excepción lanzada cuando un usuario intenta acceder a un recurso sin los permisos necesarios
public class UnauthorizedException extends RuntimeException {

    // Inicializa la excepción indicando el motivo del fallo en la autorización
    public UnauthorizedException(String message) {
        super(message);
    }
}
