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

import com.example.c196.Entity.Course;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.List;

public class CourseList extends AppCompatActivity {
    private TextView emptyView;
    private Course course;
    int termId;
    Repository repository = new Repository(getApplication());

    boolean existingCourse = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_action_bar, menu);

        if (!existingCourse) {
            menu.findItem(R.id.coursesHome).setVisible(false);
        }
        return true;
    }

    /* Determining if courses are associated, deleting (or not deleting) the terms and displaying the
     * appropriate messages to the user.  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.termsHome:
                Intent courseHome = new Intent(CourseList.this, TermList.class);
                startActivity(courseHome);
                return true;
            case R.id.coursesHome:
              return false;
            case R.id.assessmentsHome:
                Intent assessmentHome = new Intent(CourseList.this, AssessmentsList.class);
                startActivity(assessmentHome);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.recycleview3);
        emptyView = (TextView) findViewById(R.id.empty_view3);

        Repository repository = new Repository(getApplication());
        List<Course> courses = repository.getCourses();
        termId = getIntent().getIntExtra("termId", -1);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(courses);

        if (courses.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void addCourse(View view) {
        Intent intent = new Intent(CourseList.this, DetailedCourse.class);
        startActivity(intent);
    }

    public boolean OnOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreationOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_action_bar, menu);
        return true;
    }
}