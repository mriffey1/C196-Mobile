package com.example.c196.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.c196.Entity.Instructor;
import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
// PROBABLY NO LONGER NEED
public class AddCourse extends AppCompatActivity {
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
    private Repository repository = new Repository(getApplication());
    EditText courseTitle;
    EditText courseStartDate;
    EditText courseEndDate;
    Spinner courseStatus;
    Spinner courseInstructor;
    EditText courseNotes;
    Button courseSaveBtn;
    Button newInstructor;
    int selectedInstructor;

    DatePickerDialog.OnDateSetListener listenStart;
    DatePickerDialog.OnDateSetListener listenEnd;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseTitle = findViewById(R.id.courseTitle);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        courseInstructor = findViewById(R.id.courseInstructor);
        courseNotes = findViewById(R.id.courseNotes);
        courseSaveBtn = findViewById(R.id.courseSaveBtn);
        newInstructor = findViewById(R.id.newInstructor);
        courseStartDate.setText("select an start date");
        courseEndDate.setText("select an end date");

        // Spinner for Course Status
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.courseStatusString, android.R.layout.simple_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatus.setAdapter(statusAdapter);


        newInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, AddInstructor.class);
            //    Intent intent = new Intent(AddCourse.this, AddInstructor.class);

                startActivity(intent);
            }
        });

        // Spinner for Instructors

        List<Instructor> instructorList = repository.getInstructors();
        ArrayAdapter<Instructor> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, instructorList);
        courseInstructor.getAdapter();
        courseInstructor.setAdapter(typeAdapter);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




        courseInstructor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedInstructor = instructorList.get(i).getInstructorId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = courseStartDate.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddCourse.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
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

        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = courseEndDate.getText().toString();
                try {
                    calendarEnd.setTime(format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddCourse.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
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
        courseSaveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int courseId = 0;
                if (repository.getCourses().size() == 0) {
                    courseId = 1;
                } else {
                    courseId = repository.getCourses().get(repository.getCourses().size() - 1).getCourseId() + 1;
                }
                String title = courseTitle.getText().toString();
                String dateStart = courseStartDate.getText().toString();
                String dateEnd = courseEndDate.getText().toString();
                Date finalStart = null;
                Date finalEnd = null;
                String status = courseStatus.getSelectedItem().toString();
                String notes = courseNotes.getText().toString();
                int termId = 0;
                try {
                    finalStart = format1.parse(dateStart);
                    finalEnd = format1.parse(dateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Course course = new Course(courseId, title, finalStart, finalEnd, status, selectedInstructor, notes, termId);
                repository.insert(course);


                Intent intent = new Intent(AddCourse.this, CourseList.class);
                startActivity(intent);
            }
        });
    }



    // Method to update labels for start and end date
    private void updateLabel(boolean value) {
        if (value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            courseStartDate.setText(format1.format(calendarStart.getTime()));
        } else if (!value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            courseEndDate.setText(format1.format(calendarEnd.getTime()));
        }
    }


}
