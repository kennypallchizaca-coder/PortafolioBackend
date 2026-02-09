package com.lexisware.portafolio.dashboard.dtos;

// DTO para representar el registro de usuarios agrupado por periodo temporal
public class UserGrowthStats {
    private Integer month; // Número del mes (1-12)
    private Integer year; // Año del registro
    private Long count; // Cantidad de usuarios registrados en este periodo

    public UserGrowthStats() {
    }

    // Inicializa el DTO con el mes, año y cantidad de usuarios correspondiente
    public UserGrowthStats(Integer month, Integer year, Long count) {
        this.month = month;
        this.year = year;
        this.count = count;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
