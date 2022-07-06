package com.example.c196.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.c196.Entity.Course;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTerm extends AppCompatActivity {
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
    int termId;
    private Repository repo = new Repository(getApplication());
    EditText startDateTerm;
    EditText termTitle;
    EditText endDateTerm;
    Button termAddCourseBtn;
    Spinner courseSpinner;
    Button termRemoveCourseBtn;
    Button addTermSaveBtn;
    Course selectedCourse;

    DatePickerDialog.OnDateSetListener listenStart;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener listenEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        termId = getIntent().getIntExtra("id", -1);
        termTitle = findViewById(R.id.termTitle);
        addTermSaveBtn = findViewById(R.id.addTermSaveBtn);
        startDateTerm = findViewById(R.id.startDateTerm);
        endDateTerm = findViewById(R.id.endDateTerm);
        courseSpinner = findViewById(R.id.courseSpinner);

        List<Course> courseList = repo.getCourses();
        ArrayAdapter<Course> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(typeAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCourse = courseList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        startDateTerm.setText("select an start date");
        endDateTerm.setText("select an end date");
        startDateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = startDateTerm.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTerm.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
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

        endDateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = endDateTerm.getText().toString();
                try {
                    calendarEnd.setTime(format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTerm.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
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
    }

    // Method to update labels for start and end date
    private void updateLabel(boolean value) {
        if (value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            startDateTerm.setText(format1.format(calendarStart.getTime()));
        } else if (!value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            endDateTerm.setText(format1.format(calendarEnd.getTime()));
        }
    }
}
