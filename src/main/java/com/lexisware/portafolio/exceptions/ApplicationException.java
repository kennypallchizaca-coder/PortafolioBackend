package com.lexisware.portafolio.exceptions;

import org.springframework.http.HttpStatus;

// Excepción base personalizada para errores de la aplicación que incluyen un código HTTP
public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;

    // Inicializa la excepción con un mensaje descriptivo y un estado HTTP
    // específico
    public ApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    // Inicializa la excepción con mensaje, estado HTTP y la causa raíz del error
    public ApplicationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
