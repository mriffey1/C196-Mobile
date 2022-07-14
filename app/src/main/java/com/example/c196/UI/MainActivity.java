package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Course;
import com.example.c196.Entity.Instructor;
import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static int notificationAlertNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewTerms(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }

    public void viewCourses(View view) {
        Intent intent = new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
    }

    public void viewAssessments(View view) {
        Intent intent = new Intent(MainActivity.this, AssessmentsList.class);
        startActivity(intent);
    }
}