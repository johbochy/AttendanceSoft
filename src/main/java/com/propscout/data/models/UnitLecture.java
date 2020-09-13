package com.propscout.data.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UnitLecture {

    private SimpleIntegerProperty lectureId;
    private SimpleStringProperty weekday;
    private SimpleIntegerProperty duration;
    private SimpleStringProperty commencedAt;
    private SimpleStringProperty unitCode;

    public UnitLecture(int lectureId, String weekday, int duration, String commencedAt, String unitCode) {
        this.lectureId = new SimpleIntegerProperty(lectureId);
        this.weekday = new SimpleStringProperty(weekday);
        this.duration = new SimpleIntegerProperty(duration);
        this.commencedAt = new SimpleStringProperty(commencedAt);
        this.unitCode = new SimpleStringProperty(unitCode);
    }

    public String getWeekday() {
        return weekday.get();
    }

    public int getDuration() {
        return duration.get();
    }

    public String getCommencedAt() {
        return commencedAt.get();
    }

    public String getUnitCode() {
        return unitCode.get();
    }

    public int getLectureId() {
        return lectureId.get();
    }
}
