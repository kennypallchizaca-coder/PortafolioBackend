package com.lexisware.portafolio.files.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lexisware.portafolio.exceptions.ApplicationException;
import com.lexisware.portafolio.files.entities.FileEntity;
import com.lexisware.portafolio.files.repositories.FileRepository;
import com.lexisware.portafolio.files.services.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

// Implementación del servicio de Cloudinary para la gestión de imágenes
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinary;
    private final FileRepository fileRepository;

    public CloudinaryServiceImpl(Cloudinary cloudinary, FileRepository fileRepository) {
        this.cloudinary = cloudinary;
        this.fileRepository = fileRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String subirImagen(MultipartFile file, String folder) {
        try {
            if (file.isEmpty()) {
                throw new ApplicationException("El archivo está vacío", HttpStatus.BAD_REQUEST);
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ApplicationException("El archivo debe ser una imagen", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "portafolio/" + folder,
                            "resource_type", "image"));

            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            // Registra la subida en la base de datos local
            fileRepository.save(new FileEntity(imageUrl, publicId, folder));

            log.info("Imagen subida y registrada exitosamente: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Error al subir imagen: {}", e.getMessage());
            throw new ApplicationException("Error al subir imagen a Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void eliminarImagen(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Elimina el registro local si existe
            fileRepository.findByPublicId(publicId).ifPresent(fileRepository::delete);

            log.info("Imagen eliminada de la nube y registro local: {}", publicId);
        } catch (IOException e) {
            log.error("Error al eliminar imagen: {}", e.getMessage());
        }
    }

    @Override
    public String extraerIdPublico(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2)
                return null;

            String afterUpload = parts[1];
            afterUpload = afterUpload.replaceFirst("v\\d+/", "");
            return afterUpload.substring(0, afterUpload.lastIndexOf('.'));
        } catch (Exception e) {
            log.error("Error al extraer publicId: {}", e.getMessage());
            return null;
        }
    }
}
