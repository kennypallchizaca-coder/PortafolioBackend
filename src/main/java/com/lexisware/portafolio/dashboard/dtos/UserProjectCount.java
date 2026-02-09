package com.lexisware.portafolio.dashboard.dtos;

// DTO que vincula a un usuario con su cantidad total de proyectos creados
public class UserProjectCount {
    private String userName; // Nombre para mostrar del usuario
    private long projectCount; // Total de proyectos persistidos

    public UserProjectCount() {
    }

    // Inicializa el DTO con el nombre del usuario y su contador de proyectos
    public UserProjectCount(String userName, long projectCount) {
        this.userName = userName;
        this.projectCount = projectCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(long projectCount) {
        this.projectCount = projectCount;
    }
}
