package com.lexisware.portafolio.dashboard.dtos;

// DTO que consolida las métricas globales mostradas en el dashboard principal
public class DashboardStats {
    private long programmersCount;
    private long projectsCount;
    private long advisoriesPending;
    private long advisoriesApproved;
    private long advisoriesRejected;

    public DashboardStats() {
    }

    // Inicializa el resumen estadístico con los contadores de cada métrica
    public DashboardStats(long programmersCount, long projectsCount, long advisoriesPending, long advisoriesApproved,
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long programmersCount;
        private long projectsCount;
        private long advisoriesPending;
        private long advisoriesApproved;
        private long advisoriesRejected;

        public Builder programmersCount(long programmersCount) {
            this.programmersCount = programmersCount;
            return this;
        }

        public Builder projectsCount(long projectsCount) {
            this.projectsCount = projectsCount;
            return this;
        }

        public Builder advisoriesPending(long advisoriesPending) {
            this.advisoriesPending = advisoriesPending;
            return this;
        }

        public Builder advisoriesApproved(long advisoriesApproved) {
            this.advisoriesApproved = advisoriesApproved;
            return this;
        }

        public Builder advisoriesRejected(long advisoriesRejected) {
            this.advisoriesRejected = advisoriesRejected;
            return this;
        }

        public DashboardStats build() {
            return new DashboardStats(programmersCount, projectsCount, advisoriesPending, advisoriesApproved,
                    advisoriesRejected);
        }
    }
}
