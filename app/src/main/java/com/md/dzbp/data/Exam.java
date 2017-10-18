package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 考试科目
 */
public class Exam implements Serializable{
    private  String date;
    private String time;
    private String course;
    private String teacher;

    public Exam() {
    }

    public Exam(String date, String time, String course, String teacher) {
        this.date = date;
        this.time = time;
        this.course = course;
        this.teacher = teacher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
