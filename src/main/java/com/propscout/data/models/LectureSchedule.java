package com.propscout.data.models;

public class LectureSchedule {

    private int lectureId;
    private int scheduleId;
    private int unitId;
    private String startTime;
    private int duration;
    private String weekday;
    private int lectureNumber;
    private String commenceAt;


    public LectureSchedule(int lectureId, int scheduleId, int unitId, String startTime, int duration, String weekday, int lectureNumber, String commenceAt) {
        this.lectureId = lectureId;
        this.scheduleId = scheduleId;
        this.unitId = unitId;
        this.startTime = startTime;
        this.duration = duration;
        this.weekday = weekday;
        this.lectureNumber = lectureNumber;
        this.commenceAt = commenceAt;
    }

    public int getLectureId() {
        return lectureId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getWeekday() {
        return weekday;
    }

    public int getLectureNumber() {
        return lectureNumber;
    }

    public String getCommenceAt() {
        return commenceAt;
    }
}
