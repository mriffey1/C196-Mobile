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

import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.List;

public class TermList extends AppCompatActivity {
    private TextView emptyView;
    boolean existingTerm = false;

    /* Menu to navigate between the Assessment List, Term List and Course List without having
     * to return to the home screen. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_action_bar, menu);
        if (!existingTerm) {
            menu.findItem(R.id.termsHome).setVisible(false);
        }
        return true;
    }

    /* Menu to navigate between the Assessment List, Term List and Course List without having
     * to return to the home screen. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.termsHome:
                return false;
            case R.id.coursesHome:
                Intent courseHome = new Intent(TermList.this, CourseList.class);
                startActivity(courseHome);
                return true;
            case R.id.assessmentsHome:
                Intent assessmentHome = new Intent(TermList.this, AssessmentsList.class);
                startActivity(assessmentHome);
                return true;
            case R.id.instructorsHome:
                Intent instructorsHome = new Intent(TermList.this, InstructorList.class);
                startActivity(instructorsHome);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        /* RecycleView for term list and empty view when no term data is present. */
        RecyclerView recyclerView = findViewById(R.id.recycleview);
        emptyView = (TextView) findViewById(R.id.empty_view);
        Repository repository = new Repository(getApplication());
        List<Term> terms = repository.getTerms();
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(terms);
        if (terms.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    /* Add term button to add a new term into the database. */
    public void addTerm(View view) {
        Intent intent = new Intent(TermList.this, DetailedTerm.class);
        startActivity(intent);
    }
}
