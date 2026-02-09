package com.lexisware.portafolio.exceptions;

// Excepción lanzada cuando no se encuentra un recurso solicitado en el sistema
public class ResourceNotFoundException extends RuntimeException {

    // Inicializa la excepción únicamente con un mensaje de error personalizado
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Inicializa la excepción construyendo un mensaje detallado a partir del
    // recurso y el campo buscado
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s no encontrado con %s: '%s'", resource, field, value));
    }
}
