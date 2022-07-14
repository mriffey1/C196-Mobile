package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.c196.R;



public class MainActivity extends AppCompatActivity {
    public static int notificationAlertNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    /* Intent activity for button to view Term List */
    public void viewTerms(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }
    /* Intent activity for button to view Course List */
    public void viewInstructors(View view) {
        Intent intent = new Intent(MainActivity.this, InstructorList.class);
        startActivity(intent);
    }

    /* Intent activity for button to view Course List */
    public void viewCourses(View view) {
        Intent intent = new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
    }
    /* Intent activity for button to view Assessment List */
    public void viewAssessments(View view) {
        Intent intent = new Intent(MainActivity.this, AssessmentsList.class);
        startActivity(intent);
    }
}