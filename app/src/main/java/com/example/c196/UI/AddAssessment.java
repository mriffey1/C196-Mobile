package com.example.c196.UI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.c196.Entity.Assessment;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddAssessment extends AppCompatActivity {
    int assessId;
    private Repository repo = new Repository(getApplication());
    EditText startDate;
    Spinner addAssessType;
    EditText textTitle;
    Button button4;
    DatePickerDialog.OnDateSetListener listenStart;
    final Calendar calendarStart = Calendar.getInstance();

    EditText addEndDate;
    final Calendar calendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener listenEnd;

    // Formatter for dates
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        // Spinner for Objective/Performance
        Spinner typeSpinner = (Spinner) findViewById(R.id.addAssessType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.addAssessType, android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        assessId = getIntent().getIntExtra("id", -1);
        textTitle = findViewById(R.id.textTitle);
        // Calendar's for Start and End Date


        startDate = findViewById(R.id.startDate);
        addEndDate = findViewById(R.id.addEndDate);
        button4 = findViewById(R.id.button4);
        startDate.setText("select an start date");
        addEndDate.setText("select an end date");
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = startDate.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessment.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        listenStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, monthOfYear);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(true);
            }
        };

        addEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = addEndDate.getText().toString();
                try {
                    calendarEnd.setTime(format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessment.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        listenEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                boolean end = true;
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, monthOfYear);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(false);
            }
        };
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newId = 0;
                if (repo.getAssessments().size() == 0) {
                    newId = 1;
                } else {
                    newId = repo.getAssessments().get(repo.getAssessments().size() - 1).getAssessmentId() + 1;
                }
                String title = textTitle.getText().toString();
                int courseId = 0;
                String type = typeSpinner.getSelectedItem().toString();

                String dateStart = startDate.getText().toString();
                String dateEnd = addEndDate.getText().toString();
                Date finalStart = null;
                Date finalEnd = null;

                try {
                    finalStart = format1.parse(dateStart);
                    finalEnd = format1.parse(dateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Assessment assessment = new Assessment(newId, title, finalStart, finalEnd, type, courseId);
                repo.insert(assessment);
            }
        });
    }

    // Method to update labels for start and end date
    private void updateLabel(boolean value) {
        if (value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            startDate.setText(format1.format(calendarStart.getTime()));
        } else if (!value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            addEndDate.setText(format1.format(calendarEnd.getTime()));
        }
    }


}
