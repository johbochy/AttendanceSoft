package com.propscout.data.models;

public class UnitStudent {

    private String name;
    private String regNo;
    private String mobile;
    private String course;
    private int semester;
    private int year;

    public UnitStudent(String name, String regNo, String mobile, String course, int semester, int year) {
        this.name = name;
        this.regNo = regNo;
        this.mobile = mobile;
        this.course = course;
        this.semester = semester;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCourse() {
        return course;
    }

    public int getSemester() {
        return semester;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "UnitStudent{" +
                "name='" + name + '\'' +
                ", regNo='" + regNo + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
