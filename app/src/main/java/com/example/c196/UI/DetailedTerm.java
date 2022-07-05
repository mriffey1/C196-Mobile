package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.c196.R;

public class DetailedTerm extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    String title;
    int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);
        editTitle = findViewById(R.id.editTitle);
        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        termId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        editTitle.setText(title);
    }
}