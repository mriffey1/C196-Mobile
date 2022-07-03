package com.example.c196.Enitiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String assessmentTitle;
    private Date assessmentStart;
    private Date assessmentEnd;
    private String assessmentType;
    private int courseId;
}
