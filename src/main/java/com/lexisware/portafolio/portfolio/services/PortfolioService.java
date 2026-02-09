package com.lexisware.portafolio.portfolio.services;

import com.lexisware.portafolio.portfolio.models.Portfolio;
import java.util.List;

// Interfaz para gestión de portafolios
public interface PortfolioService {

    // Obtiene portafolios públicos
    List<Portfolio> obtenerPortafoliosPublicos();

    // Busca portafolio por ID
    Portfolio obtenerPortafolioPorId(Long id);

    // Obtiene portafolio por usuario
    Portfolio obtenerPortafolioPorUsuario(String userId);

    // Crea portafolio
    Portfolio crearPortafolio(Portfolio portfolioModel);

    // Actualiza portafolio
    Portfolio actualizarPortafolio(Long id, Portfolio portfolioUpdateModel, String requestUserUid);

    // Elimina portafolio
    void eliminarPortafolio(Long id, String requestUserUid);
}
