package com.faculty.model;

import java.time.LocalTime;

public class TimetableEntry {
    private int id;
    private int courseCode;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    // joined
    private String courseName;

    public TimetableEntry() {}

    public TimetableEntry(int id, int courseCode, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.courseCode = courseCode;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCourseCode() { return courseCode; }
    public void setCourseCode(int courseCode) { this.courseCode = courseCode; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}
