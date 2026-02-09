package com.lexisware.portafolio.users.services;

import com.lexisware.portafolio.users.models.User;
import java.util.List;

// Definición de las operaciones de negocio para la gestión integral de perfiles de usuario
public interface UserService {

    // Obtiene una lista detallada de todos los usuarios con rol de programador
    List<User> obtenerProgramadores();

    // Filtra y lista a los programadores que se encuentran actualmente disponibles
    List<User> obtenerProgramadoresDisponibles();

    // Localiza la información profesional de un usuario por su identificador UID
    User obtenerUsuarioPorId(String uid);

    // Procesa el registro o la modificación completa de los datos de un usuario
    User crearOActualizarUsuario(User userModel);

    // Modifica específicamente el estado de disponibilidad de un usuario en el
    // sistema
    User actualizarDisponibilidad(String uid, boolean available);

    // Elimina de forma definitiva al usuario correspondiente al identificador UID
    void eliminarUsuario(String uid);
}
