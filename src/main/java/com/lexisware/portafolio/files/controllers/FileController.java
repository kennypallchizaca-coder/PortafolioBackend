package com.lexisware.portafolio.files.controllers;

import com.lexisware.portafolio.files.dtos.UploadResponseDto;
import com.lexisware.portafolio.files.services.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Controlador para gestionar la subida y eliminación de archivos multimedia en la nube
@RestController
@RequestMapping("/api/files")
@Tag(name = "Archivos", description = "Gestión de archivos e imágenes")
public class FileController {

    private final CloudinaryService cloudinaryService;

    // Inicializa el controlador con el servicio de integración para almacenamiento
    // externo
    public FileController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // Gestiona la subida de una imagen destinada al perfil del usuario y retorna su
    // URL
    @PostMapping("/upload/profile")
    @Operation(summary = "Subir imagen de perfil", description = "Sube una imagen de perfil a Cloudinary")
    public ResponseEntity<UploadResponseDto> subirImagenPerfil(
            @RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.subirImagen(file, "profiles");
        return ResponseEntity.ok(
                UploadResponseDto.builder()
                        .url(imageUrl)
                        .message("Imagen de perfil subida exitosamente")
                        .build());
    }

    // Procesa la subida de capturas o recursos visuales para proyectos y retorna la
    // URL generada
    @PostMapping("/upload/project")
    @Operation(summary = "Subir imagen de proyecto", description = "Sube una imagen de proyecto a Cloudinary")
    public ResponseEntity<UploadResponseDto> subirImagenProyecto(
            @RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.subirImagen(file, "projects");
        return ResponseEntity.ok(
                UploadResponseDto.builder()
                        .url(imageUrl)
                        .message("Imagen de proyecto subida exitosamente")
                        .build());
    }

    // Solicita la eliminación permanente de un recurso multimedia mediante su
    // identificador único
    @DeleteMapping
    @Operation(summary = "Eliminar imagen", description = "Elimina una imagen de Cloudinary usando su publicId")
    public ResponseEntity<Void> eliminarImagen(@RequestParam String publicId) {
        cloudinaryService.eliminarImagen(publicId);
        return ResponseEntity.noContent().build();
    }
}
