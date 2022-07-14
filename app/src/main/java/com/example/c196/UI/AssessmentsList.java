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
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.List;

public class AssessmentsList extends AppCompatActivity {
    private TextView emptyView;
    boolean existingAssessment = false;

    /* Menu to navigate between the Assessment List, Term List and Course List without having
     * to return to the home screen. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_action_bar, menu);
        if (!existingAssessment) {
            menu.findItem(R.id.assessmentsHome).setVisible(false);
        }
        return true;
    }

    /* Determining if courses are associated, deleting (or not deleting) the terms and displaying the
     * appropriate messages to the user.  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.termsHome:
                Intent courseHome = new Intent(AssessmentsList.this, TermList.class);
                startActivity(courseHome);
                return true;
            case R.id.coursesHome:
                Intent assessmentHome = new Intent(AssessmentsList.this, CourseList.class);
                startActivity(assessmentHome);
                return true;
            case R.id.assessmentsHome:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Retrieving a list of all assessments and displaying in the recycler view. If no
        assessments exist, a message will be displayed */
        RecyclerView recyclerView = findViewById(R.id.recycleview2);
        emptyView = (TextView) findViewById(R.id.empty_view2);
        Repository repository = new Repository(getApplication());
        List<Assessment> assessments = repository.getAssessments();
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(assessments);
        if (assessments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    /* Floating icon to add a new assessment */
    public void addAssessmentClick(View view) {
        Intent intent = new Intent(AssessmentsList.this, DetailedAssessments.class);
        startActivity(intent);

    }
}