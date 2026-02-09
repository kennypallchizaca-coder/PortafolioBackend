package com.lexisware.portafolio.users.controllers;

import com.lexisware.portafolio.users.services.UserService;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.users.dtos.UserResponseDto;
import com.lexisware.portafolio.users.dtos.UserUpdateRequestDto;
import com.lexisware.portafolio.users.models.User;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// Controlador para gestionar perfiles, disponibilidad y permisos de usuarios
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Inicializa el controlador con los servicios de usuario y mapeo de datos
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Retorna un listado de todos los usuarios registrados con el rol de
    // programador
    @GetMapping("/programmers")
    public ResponseEntity<List<UserResponseDto>> obtenerTodosLosProgramadores() {
        List<User> users = userService.obtenerProgramadores();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Lista únicamente los programadores que han marcado su estado como disponible
    @GetMapping("/programmers/available")
    public ResponseEntity<List<UserResponseDto>> obtenerProgramadoresDisponibles() {
        List<User> users = userService.obtenerProgramadoresDisponibles();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Recupera la información del perfil completo para el usuario que inició sesión
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> obtenerUsuarioActual(@AuthenticationPrincipal String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Consulta los detalles de un usuario específico mediante su identificador UID
    @GetMapping("/{uid}")
    public ResponseEntity<UserResponseDto> obtenerUsuarioPorId(@PathVariable("uid") String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Registra un nuevo usuario o sobreescribe uno existente en el almacenamiento
    @PostMapping
    public ResponseEntity<UserResponseDto> crearOActualizarUsuario(@RequestBody User user) {
        User saved = userService.crearOActualizarUsuario(user);
        return ResponseEntity.ok(userMapper.toResponseDto(saved));
    }

    // Aplica cambios parciales al perfil del usuario validando la integridad de los
    // datos
    @PatchMapping("/{uid}")
    public ResponseEntity<UserResponseDto> actualizarUsuario(
            @PathVariable("uid") String uid,
            @Valid @RequestBody UserUpdateRequestDto updateRequest) {

        User user = userService.obtenerUsuarioPorId(uid);
        userMapper.updateModel(user, updateRequest);
        User updated = userService.crearOActualizarUsuario(user);

        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Actualiza el estado de disponibilidad del programador para recibir asesorías
    @PatchMapping("/{uid}/availability")
    public ResponseEntity<UserResponseDto> actualizarDisponibilidad(
            @PathVariable("uid") String uid,
            @RequestBody Map<String, Boolean> body) {
        boolean available = body.getOrDefault("available", false);
        User updated = userService.actualizarDisponibilidad(uid, available);
        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Remueve de forma permanente a un usuario del sistema mediante su UID
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("uid") String uid) {
        userService.eliminarUsuario(uid);
        return ResponseEntity.noContent().build();
    }
}
