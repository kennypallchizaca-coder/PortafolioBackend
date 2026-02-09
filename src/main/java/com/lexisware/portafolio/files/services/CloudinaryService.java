package com.lexisware.portafolio.files.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lexisware.portafolio.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

// Servicio de gestión de imágenes en Cloudinary
@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Sube imagen a Cloudinary
    @SuppressWarnings("unchecked")
    public String subirImagen(MultipartFile file, String folder) {
        try {
            // Valida archivo no vacío
            if (file.isEmpty()) {
                throw new ApplicationException(
                        "El archivo está vacío",
                        HttpStatus.BAD_REQUEST);
            }

            // Valida tipo imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ApplicationException(
                        "El archivo debe ser una imagen",
                        HttpStatus.BAD_REQUEST);
            }

            // Sube archivo
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "portafolio/" + folder,
                            "resource_type", "image"));

            // Obtiene URL segura
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Imagen subida exitosamente: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            // Manejo de errores
            log.error("Error al subir imagen: {}", e.getMessage());
            throw new ApplicationException(
                    "Error al subir imagen a Cloudinary",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Elimina imagen de Cloudinary
    public void eliminarImagen(String publicId) {
        try {
            // Elimina recurso por publicId
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Imagen eliminada: {}", publicId);
        } catch (IOException e) {
            // Captura errores
            log.error("Error al eliminar imagen: {}", e.getMessage());
        }
    }

    // Extrae public ID de URL
    public String extraerIdPublico(String imageUrl) {
        // Valida URL
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // Parsea URL
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2)
                return null;

            // Extrae parte posterior
            String afterUpload = parts[1];

            // Remueve versión
            afterUpload = afterUpload.replaceFirst("v\\d+/", "");

            // Remueve extensión
            return afterUpload.substring(0, afterUpload.lastIndexOf('.'));
        } catch (Exception e) {
            // Manejo de errores de parseo
            log.error("Error al extraer publicId: {}", e.getMessage());
            return null;
        }
    }
}
