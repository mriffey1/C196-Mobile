package com.example.c196.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.c196.R;
import com.example.c196.db.Repository;

import java.util.Calendar;
import java.util.Date;

public class AddAssessment extends AppCompatActivity {
    private Repository repo = new Repository(getApplication());
    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener addEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
    }

    public void onSave(View view) {
    }
}
