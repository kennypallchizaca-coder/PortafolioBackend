package com.lexisware.portafolio.dashboard.dtos;

public class UserProjectCount {
    private String userName;
    private long projectCount;

    public UserProjectCount() {
    }

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
