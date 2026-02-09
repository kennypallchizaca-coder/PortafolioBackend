package com.lexisware.portafolio.portfolio.services;

import com.lexisware.portafolio.portfolio.models.Portfolio;
import java.util.List;

// Contrato que define las operaciones permitidas para la gestión de portafolios técnicos
public interface PortfolioService {

    // Recupera la colección de portafolios marcados con visibilidad pública
    List<Portfolio> obtenerPortafoliosPublicos();

    // Obtiene un portafolio específico utilizando su identificador técnico
    // incremental
    Portfolio obtenerPortafolioPorId(Long id);

    // Recupera el portafolio vinculado a un usuario mediante su identificador UID
    Portfolio obtenerPortafolioPorUsuario(String userId);

    // Crea y persiste un nuevo portafolio validando las reglas de negocio
    // pertinentes
    Portfolio crearPortafolio(Portfolio portfolioModel);

    // Modifica los datos de un portafolio validando que el solicitante sea el
    // propietario
    Portfolio actualizarPortafolio(Long id, Portfolio portfolioUpdateModel, String requestUserUid);

    // Elimina de forma permanente un portafolio tras verificar los permisos del
    // usuario
    void eliminarPortafolio(Long id, String requestUserUid);
}
