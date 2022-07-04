package com.example.c196.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseName;
    private Date courseStart;
    private Date courseEnd;
    private String courseStatus;
    private int instructorId;
    private String courseOptionalNotes;
    private int termId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(Date courseStart) {
        this.courseStart = courseStart;
    }

    public Date getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(Date courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getCourseOptionalNotes() {
        return courseOptionalNotes;
    }

    public void setCourseOptionalNotes(String courseOptionalNotes) {
        this.courseOptionalNotes = courseOptionalNotes;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public Course(int courseId, String courseName, Date courseStart, Date courseEnd, String courseStatus, int instructorId, String courseOptionalNotes, int termId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.instructorId = instructorId;
        this.courseOptionalNotes = courseOptionalNotes;
        this.termId = termId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseStart=" + courseStart +
                ", courseEnd=" + courseEnd +
                ", courseStatus='" + courseStatus + '\'' +
                ", instructorId=" + instructorId +
                ", courseOptionalNotes='" + courseOptionalNotes + '\'' +
                ", termId=" + termId +
                '}';
    }
}
