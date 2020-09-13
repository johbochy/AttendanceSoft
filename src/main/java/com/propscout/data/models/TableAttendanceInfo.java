package com.propscout.data.models;

import javafx.beans.property.SimpleStringProperty;

public class TableAttendanceInfo {

    private SimpleStringProperty name;
    private SimpleStringProperty week1;
    private SimpleStringProperty week2;
    private SimpleStringProperty week3;
    private SimpleStringProperty week4;
    private SimpleStringProperty week5;

    public TableAttendanceInfo(String name, String week1, String week2, String week3, String week4, String week5) {
        this.name = new SimpleStringProperty(name);
        this.week1 = new SimpleStringProperty(week1);
        this.week2 = new SimpleStringProperty(week2);
        this.week3 = new SimpleStringProperty(week3);
        this.week4 = new SimpleStringProperty(week4);
        this.week5 = new SimpleStringProperty(week5);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getWeek1() {
        return week1.get();
    }

    public void setWeek1(String week1) {
        this.week1.set(week1);
    }

    public String getWeek2() {
        return week2.get();
    }

    public void setWeek2(String week2) {
        this.week2.set(week2);
    }

    public String getWeek3() {
        return week3.get();
    }

    public void setWeek3(String week3) {
        this.week3.set(week3);
    }

    public String getWeek4() {
        return week4.get();
    }

    public void setWeek4(String week4) {
        this.week4.set(week4);
    }

    public String getWeek5() {
        return week5.get();
    }

    public void setWeek5(String week5) {
        this.week5.set(week5);
    }
}
