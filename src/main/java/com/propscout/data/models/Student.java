package com.propscout.data.models;

public class Student {

    private int id;
    private String regNo;
    private String name;
    private String mobile;
    private int courseId;
    private int year;
    private int semester;
    private byte[] avatar;
    private byte[] fingerprint;

    public Student() {
    }

    public Student(int id, String regNo, String name, String mobile, int courseId, int year, int semester, byte[] avatar, byte[] fingerprint) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.mobile = mobile;
        this.courseId = courseId;
        this.year = year;
        this.semester = semester;
        this.avatar = avatar;
        this.fingerprint = fingerprint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
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
}
