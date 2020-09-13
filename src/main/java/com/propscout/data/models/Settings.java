package com.propscout.data.models;

import java.time.LocalDate;

public class Settings {

    private LocalDate commenceDate;
    private LocalDate endDate;
    private int semester;
    private int id;

    public Settings(LocalDate commenceDate, LocalDate endDate, int semester) {
        this.commenceDate = commenceDate;
        this.endDate = endDate;
        this.semester = semester;
    }

    public LocalDate getCommenceDate() {
        return commenceDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getSemester() {
        return semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "commenceDate=" + commenceDate +
                ", endDate=" + endDate +
                ", semester=" + semester +
                ", id=" + id +
                '}';
    }
}
