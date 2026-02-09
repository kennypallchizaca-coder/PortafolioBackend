package com.lexisware.portafolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// Punto de entrada principal y configuración base de la aplicación Spring Boot
@SpringBootApplication
@EnableScheduling
public class PortafolioBackendApplication {

    // Inicia la ejecución del servidor y el contexto de dependencias de Spring
    public static void main(String[] args) {
        SpringApplication.run(PortafolioBackendApplication.class, args);
    }
}
