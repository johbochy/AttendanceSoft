package com.propscout.data.models;

public class Attendance {

    private int lectureId;
    private String regNo;
    private String arrivedAt;

    public Attendance(int lectureId, String regNo, String arrivedAt) {
        this.lectureId = lectureId;
        this.regNo = regNo;
        this.arrivedAt = arrivedAt;
    }

    public int getLectureId() {
        return lectureId;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getArrivedAt() {
        return arrivedAt;
    }
}
