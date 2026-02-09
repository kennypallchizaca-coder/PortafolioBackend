package com.lexisware.portafolio.exceptions;

// Excepción no autorizado
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
