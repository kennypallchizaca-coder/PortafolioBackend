package com.lexisware.portafolio.users.services;

import com.lexisware.portafolio.users.models.User;
import java.util.List;

// Interfaz para gestión de usuarios y perfiles
public interface UserService {

    // Obtiene usuarios programadores
    List<User> obtenerProgramadores();

    // Obtiene programadores disponibles
    List<User> obtenerProgramadoresDisponibles();

    // Busca usuario por UID
    User obtenerUsuarioPorId(String uid);

    // Crea o actualiza usuario
    User crearOActualizarUsuario(User userModel);

    // Actualiza disponibilidad
    User actualizarDisponibilidad(String uid, boolean available);

    // Elimina usuario por ID
    void eliminarUsuario(String uid);
}
