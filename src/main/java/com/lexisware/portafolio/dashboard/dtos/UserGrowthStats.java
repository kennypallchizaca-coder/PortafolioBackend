package com.lexisware.portafolio.dashboard.dtos;

public class UserGrowthStats {
    private Integer month; // 1-12
    private Integer year;
    private Long count;

    public UserGrowthStats() {
    }

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
