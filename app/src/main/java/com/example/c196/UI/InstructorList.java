package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Instructor;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.List;

public class InstructorList extends AppCompatActivity {
    private TextView emptyView;
    boolean existingInstructor = false;

    /* Menu to navigate between the Assessment List, Term List and Course List without having
     * to return to the home screen. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_action_bar, menu);
        if (!existingInstructor) {
            menu.findItem(R.id.instructorsHome).setVisible(false);
        }
        return true;
    }

    /* Menu to navigate between the Assessment List, Term List and Course List without having
     * to return to the home screen. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.termsHome:
                Intent termsHome = new Intent(InstructorList.this, TermList.class);
                startActivity(termsHome);
                return true;
            case R.id.coursesHome:
                Intent courseHome = new Intent(InstructorList.this, CourseList.class);
                startActivity(courseHome);
                return true;
            case R.id.assessmentsHome:
                Intent assessmentHome = new Intent(InstructorList.this, AssessmentsList.class);
                startActivity(assessmentHome);
                return true;
            case R.id.instructorsHome:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Retrieving a list of all assessments and displaying in the recycler view. If no
        assessments exist, a message will be displayed */
        RecyclerView recyclerView = findViewById(R.id.instructorRecycle);
        emptyView = (TextView) findViewById(R.id.instructorEmptyView);
        Repository repository = new Repository(getApplication());
        List<Instructor> instructors = repository.getInstructors();
        final InstructorAdapter adapter = new InstructorAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setInstructors(instructors);
        if (instructors.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    /* Floating icon to add a new assessment */
    public void addInstructor(View view) {
        Intent intent = new Intent(InstructorList.this, AddInstructor.class);
        startActivity(intent);

    }
}