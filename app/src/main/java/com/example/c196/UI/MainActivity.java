package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewTerms(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());
    }

    public void viewCourses(View view) {
        Intent intent = new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());
    }

    public void viewAssessments(View view) {
        Intent intent = new Intent(MainActivity.this, AssessmentsList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());
    }
}