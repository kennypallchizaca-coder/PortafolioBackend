package com.lexisware.portafolio.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Expositor de puntos de acceso públicos para verificar el estado y la metadata de la API
@RestController
@RequestMapping("/api/public")
public class PublicController {

    // Verifica la disponibilidad operativa de los servicios del backend
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "LEXISWARE Portfolio Backend");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    // Retorna información descriptiva general sobre el propósito y autoría de la
    // aplicación
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "LEXISWARE Portfolio API");
        info.put("description", "Backend para sistema de portafolios");
        info.put("version", "1.0.0");
        info.put("author", "LEXIS-TEAM");
        return ResponseEntity.ok(info);
    }
}
