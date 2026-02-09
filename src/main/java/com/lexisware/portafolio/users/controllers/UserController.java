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

// Controlador para gestión de usuarios
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Listar todos los programadores
    @GetMapping("/programmers")
    public ResponseEntity<List<UserResponseDto>> obtenerTodosLosProgramadores() {
        List<User> users = userService.obtenerProgramadores();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Listar programadores disponibles para asesoría
    @GetMapping("/programmers/available")
    public ResponseEntity<List<UserResponseDto>> obtenerProgramadoresDisponibles() {
        List<User> users = userService.obtenerProgramadoresDisponibles();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Obtener perfil del usuario actual
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> obtenerUsuarioActual(@AuthenticationPrincipal String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Obtener usuario por ID
    @GetMapping("/{uid}")
    public ResponseEntity<UserResponseDto> obtenerUsuarioPorId(@PathVariable("uid") String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Crear o actualizar usuario completo
    @PostMapping
    public ResponseEntity<UserResponseDto> crearOActualizarUsuario(@RequestBody User user) {
        User saved = userService.crearOActualizarUsuario(user);
        return ResponseEntity.ok(userMapper.toResponseDto(saved));
    }

    // Actualización parcial de perfil
    @PatchMapping("/{uid}")
    public ResponseEntity<UserResponseDto> actualizarUsuario(
            @PathVariable("uid") String uid,
            @Valid @RequestBody UserUpdateRequestDto updateRequest) {

        User user = userService.obtenerUsuarioPorId(uid);
        userMapper.updateModel(user, updateRequest);
        User updated = userService.crearOActualizarUsuario(user);

        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Actualizar disponibilidad
    @PatchMapping("/{uid}/availability")
    public ResponseEntity<UserResponseDto> actualizarDisponibilidad(
            @PathVariable("uid") String uid,
            @RequestBody Map<String, Boolean> body) {
        boolean available = body.getOrDefault("available", false);
        User updated = userService.actualizarDisponibilidad(uid, available);
        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Eliminar usuario
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("uid") String uid) {
        userService.eliminarUsuario(uid);
        return ResponseEntity.noContent().build();
    }
}
