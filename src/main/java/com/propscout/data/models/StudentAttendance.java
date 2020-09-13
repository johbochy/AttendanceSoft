package com.propscout.data.models;

import javafx.beans.property.SimpleStringProperty;

public class StudentAttendance {

    private final SimpleStringProperty regNo;
    private final SimpleStringProperty name;
    private final SimpleStringProperty mobile;
    private final SimpleStringProperty arrivedAt;
    private final SimpleStringProperty delayedFor;

    public StudentAttendance(String regNo, String name, String mobile, String arrivedAt, String delayedFor) {
        this.regNo = new SimpleStringProperty(regNo);
        this.name = new SimpleStringProperty(name);
        this.mobile = new SimpleStringProperty(mobile);
        this.arrivedAt = new SimpleStringProperty(arrivedAt);
        this.delayedFor = new SimpleStringProperty(delayedFor);
    }

    public String getRegNo() {
        return regNo.get();
    }

    public String getName() {
        return name.get();
    }

    public String getMobile() {
        return mobile.get();
    }

    public String getArrivedAt() {
        return arrivedAt.get();
    }

    public String getDelayedFor() {
        return delayedFor.get();
    }
}
