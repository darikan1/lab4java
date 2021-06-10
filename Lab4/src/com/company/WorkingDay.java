package com.company;

import java.time.LocalDate;

public class WorkingDay {

    private LocalDate date;
    private long hoursCount;
    private String projectName;
    private Employee emploee;

    public WorkingDay(LocalDate date, long hoursCount, String projectName) {
        this.date = date;
        this.hoursCount = hoursCount;
        this.projectName = projectName;
    }

    public Employee getEmploee() {
        return emploee;
    }

    public void setEmploee(Employee emploee) {
        this.emploee = emploee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getHoursCount() {
        return hoursCount;
    }

    public void setHoursCount(long hoursCount) {
        this.hoursCount = hoursCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "WorkingDay{" +
                "date=" + date +
                ", hoursCount=" + hoursCount +
                ", projectName='" + projectName + '\'' +
                ", emploee=" + emploee +
                '}';
    }
}
