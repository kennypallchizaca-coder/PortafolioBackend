package com.lexisware.portafolio.files.services;

import org.springframework.web.multipart.MultipartFile;

// Interfaz para la gestión de archivos e imágenes en servicios externos
public interface CloudinaryService {

    // Sube una imagen a una carpeta específica y retorna la URL
    String subirImagen(MultipartFile file, String folder);

    // Elimina una imagen mediante su identificador público
    void eliminarImagen(String publicId);

    // Extrae el identificador público de una URL de Cloudinary
    String extraerIdPublico(String imageUrl);
}
