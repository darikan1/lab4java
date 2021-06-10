package com.company;

public class Employee {
    private String name;
    private String appointment;

    public Employee(String name, String appointment) {
        this.name = name;
        this.appointment = appointment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    @Override
    public String toString() {
        return String.format("%s appointed to %s", name, appointment);
    }
}
