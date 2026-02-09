package com.lexisware.portafolio.auth.dtos;

import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;

// Respuesta de autenticación
public class AuthResponse {
    private String token; // Token JWT
    private UserDTO user;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // DTO de usuario seguro
    public static class UserDTO {
        private String uid;
        private String email;
        private String displayName;
        private String role;
        private Boolean available;

        public UserDTO() {
        }

        public UserDTO(String uid, String email, String displayName, String role, Boolean available) {
            this.uid = uid;
            this.email = email;
            this.displayName = displayName;
            this.role = role;
            this.available = available;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Boolean getAvailable() {
            return available;
        }

        public void setAvailable(Boolean available) {
            this.available = available;
        }

        // Factory desde Entidad
        public static UserDTO fromEntity(UserEntity user) {
            return new UserDTO(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole() != null ? user.getRole().name() : null,
                    user.getAvailable());
        }

        // Factory desde Modelo
        public static UserDTO fromModel(User user) {
            return new UserDTO(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole() != null ? user.getRole().name() : null,
                    user.getAvailable());
        }
    }
}
