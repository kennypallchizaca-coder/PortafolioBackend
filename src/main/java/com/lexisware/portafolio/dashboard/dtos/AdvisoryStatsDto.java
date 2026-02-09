package com.lexisware.portafolio.dashboard.dtos;

// DTO genérico para representar conteos estadísticos con una etiqueta
public class AdvisoryStatsDto {
    private String label; // Etiqueta descriptiva (ej. mes o nombre)
    private Long count; // Cantidad total asociada

    public AdvisoryStatsDto() {
    }

    // Inicializa el DTO con una etiqueta y su correspondiente conteo
    public AdvisoryStatsDto(String label, Long count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String label;
        private Long count;

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder count(Long count) {
            this.count = count;
            return this;
        }

        public AdvisoryStatsDto build() {
            return new AdvisoryStatsDto(label, count);
        }
    }
}
