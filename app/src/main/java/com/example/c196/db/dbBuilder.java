package com.example.c196.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.c196.DAO.AssessmentDAO;
import com.example.c196.DAO.CourseDAO;
import com.example.c196.DAO.InstructorDAO;
import com.example.c196.DAO.TermDAO;
import com.example.c196.Enitiy.Assessment;
import com.example.c196.Enitiy.Course;
import com.example.c196.Enitiy.Instructor;
import com.example.c196.Enitiy.Term;

@Database(entities = {Term.class, Instructor.class, Assessment.class, Course.class}, version = 1, exportSchema = false)
public abstract class dbBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();

    public abstract InstructorDAO instructorDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract CourseDAO courseDAO();

    private static volatile dbBuilder INSTANCE;

    static dbBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (dbBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), dbBuilder.class, "c196db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}