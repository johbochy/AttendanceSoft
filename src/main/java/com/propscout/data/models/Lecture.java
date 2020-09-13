package com.propscout.data.models;

public class Lecture {
    private int id;
    private int scheduleId;
    private int lectureNumber;
    private String commencedAt;

    public Lecture(int id, int scheduleId, int lectureNumber, String commencedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.lectureNumber = lectureNumber;
        this.commencedAt = commencedAt;
    }

    public int getId() {
        return id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getLectureNumber() {
        return lectureNumber;
    }

    public String getCommencedAt() {
        return commencedAt;
    }
}
