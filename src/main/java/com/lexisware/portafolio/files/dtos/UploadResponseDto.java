package com.lexisware.portafolio.files.dtos;

// DTO que contiene la URL del archivo subido y un mensaje de confirmación
public class UploadResponseDto {
    private String url; // URL pública del recurso almacenado
    private String message; // Mensaje de éxito del servidor

    public UploadResponseDto() {
    }

    // Inicializa la respuesta con la URL del recurso y el mensaje informativo
    public UploadResponseDto(String url, String message) {
        this.url = url;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private String message;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public UploadResponseDto build() {
            return new UploadResponseDto(url, message);
        }
    }
}
