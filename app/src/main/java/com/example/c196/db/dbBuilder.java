package com.example.c196.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.c196.DAO.AssessmentDAO;
import com.example.c196.DAO.CourseDAO;
import com.example.c196.DAO.InstructorDAO;
import com.example.c196.DAO.TermDAO;
import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Course;
import com.example.c196.Entity.Instructor;
import com.example.c196.Entity.Term;

@Database(entities={Assessment.class, Course.class, Term.class, Instructor.class}, version=6, exportSchema = false)
@TypeConverters(DateConverter.class)
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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), dbBuilder.class, "c196Database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}