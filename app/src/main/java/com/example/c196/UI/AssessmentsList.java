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
import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.sql.SQLOutput;
import java.util.List;

public class AssessmentsList extends AppCompatActivity {
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public boolean onCreationOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }

    public boolean OnOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAssessmentClick(View view) {
        Intent intent = new Intent(AssessmentsList.this, DetailedAssessments.class);
        startActivity(intent);

    }
}