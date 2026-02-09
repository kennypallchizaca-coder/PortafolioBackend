package com.lexisware.portafolio.files.repositories;

import com.lexisware.portafolio.files.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repositorio para la gestión del registro de archivos en base de datos
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    // Busca un registro de archivo por su identificador público de Cloudinary
    Optional<FileEntity> findByPublicId(String publicId);
}
