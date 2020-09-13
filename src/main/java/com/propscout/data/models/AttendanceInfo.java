package com.propscout.data.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceInfo implements Serializable {

    private String name;
    private LocalDateTime arrivedAt;
    private LocalDateTime commencedAt;
    private LocalTime startTime;
    private String weekday;

    public AttendanceInfo(String name, LocalDateTime arrivedAt, LocalDateTime commencedAt, LocalTime startTime, String weekday) {
        this.name = name;
        this.arrivedAt = arrivedAt;
        this.commencedAt = commencedAt;
        this.startTime = startTime;
        this.weekday = weekday;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

    public LocalDateTime getCommencedAt() {
        return commencedAt;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getWeekday() {
        return weekday;
    }

    @Override
    public String toString() {
        return "AttendanceInfo{" +
                "name='" + name + '\'' +
                ", arrivedAt=" + arrivedAt +
                ", commencedAt=" + commencedAt +
                ", startTime=" + startTime +
                ", weekday='" + weekday + '\'' +
                '}';
    }
}
