package com.lexisware.portafolio.files.dtos;

public class UploadResponseDto {
    private String url;
    private String message;

    public UploadResponseDto() {
    }

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
