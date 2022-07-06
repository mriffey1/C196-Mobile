package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedTerm extends AppCompatActivity {
    private Repository repo = new Repository(getApplication());
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    Date startDate;
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
        Long startLong = getIntent().getLongExtra("start", -1);
        startDate = new Date(startLong);
        String startString = format1.format(startDate);
        editStartDate.setText(startString);
        editTitle.setText(title);
    }
}