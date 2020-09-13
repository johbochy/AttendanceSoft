package com.propscout.data.models;

public class UnitSchedule {

    private int id;
    private int unitId;
    private String startTime;
    private int duration;
    private String weekDay;

    public UnitSchedule() {
    }

    public UnitSchedule(int id, int unitId, String startTime, int duration, String weekDay) {
        this.id = id;
        this.unitId = unitId;
        this.startTime = startTime;
        this.duration = duration;
        this.weekDay = weekDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", unitId=" + unitId +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", weekDay='" + weekDay + '\'' +
                '}';
    }
}
