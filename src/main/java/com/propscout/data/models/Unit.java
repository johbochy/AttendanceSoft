package com.propscout.data.models;

public class Unit {

    private int id;
    private int courseId;
    private int officerId;
    private String name;
    private String code;
    private int year;
    private int semester;

    public Unit() {
    }

    public Unit(int id, int courseId, int officerId, String name, String code, int year, int semester) {
        this.id = id;
        this.courseId = courseId;
        this.officerId = officerId;
        this.name = name;
        this.code = code;
        this.year = year;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", officerId=" + officerId +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", year=" + year +
                ", semester=" + semester +
                '}';
    }
}
