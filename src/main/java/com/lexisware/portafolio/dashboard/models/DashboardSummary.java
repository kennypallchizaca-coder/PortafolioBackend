package com.lexisware.portafolio.dashboard.models;

// Modelo de dominio que agrupa los indicadores clave del estado del sistema
public class DashboardSummary {

    private long programmersCount;
    private long projectsCount;
    private long advisoriesPending;
    private long advisoriesApproved;
    private long advisoriesRejected;

    public DashboardSummary() {
    }

    public DashboardSummary(long programmersCount, long projectsCount, long advisoriesPending, long advisoriesApproved,
            long advisoriesRejected) {
        this.programmersCount = programmersCount;
        this.projectsCount = projectsCount;
        this.advisoriesPending = advisoriesPending;
        this.advisoriesApproved = advisoriesApproved;
        this.advisoriesRejected = advisoriesRejected;
    }

    public long getProgrammersCount() {
        return programmersCount;
    }

    public void setProgrammersCount(long programmersCount) {
        this.programmersCount = programmersCount;
    }

    public long getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(long projectsCount) {
        this.projectsCount = projectsCount;
    }

    public long getAdvisoriesPending() {
        return advisoriesPending;
    }

    public void setAdvisoriesPending(long advisoriesPending) {
        this.advisoriesPending = advisoriesPending;
    }

    public long getAdvisoriesApproved() {
        return advisoriesApproved;
    }

    public void setAdvisoriesApproved(long advisoriesApproved) {
        this.advisoriesApproved = advisoriesApproved;
    }

    public long getAdvisoriesRejected() {
        return advisoriesRejected;
    }

    public void setAdvisoriesRejected(long advisoriesRejected) {
        this.advisoriesRejected = advisoriesRejected;
    }
}
