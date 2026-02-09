package com.lexisware.portafolio.dashboard.entities;

import jakarta.persistence.*;

// Entidad para almacenar configuraciones personalizadas de la vista del dashboard
@Entity
@Table(name = "dashboard_config")
public class DashboardConfigEntity {

    @Id
    private String userUid;

    @Column(nullable = false)
    private boolean showNotifications;

    @Column(nullable = false)
    private String preferredTheme;

    public DashboardConfigEntity() {
    }

    public DashboardConfigEntity(String userUid, boolean showNotifications, String preferredTheme) {
        this.userUid = userUid;
        this.showNotifications = showNotifications;
        this.preferredTheme = preferredTheme;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public boolean isShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public String getPreferredTheme() {
        return preferredTheme;
    }

    public void setPreferredTheme(String preferredTheme) {
        this.preferredTheme = preferredTheme;
    }
}
