package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.c196.Entity.Course;
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

public class DetailedTerm extends AppCompatActivity {
    Repository repository = new Repository(getApplication());
    DatePickerDialog.OnDateSetListener listenStart;
    DatePickerDialog.OnDateSetListener listenEnd;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
    EditText termTitle;
    EditText startDateTerm;
    EditText endDateTerm;
    Date startDate;
    Date endDate;
    String title;
    Spinner courseSpinner;
    Button termRemoveCourseBtn;
    Button termAddCourseBtn;
    Button addTermSaveBtn;
    int termId;
    Course selectedCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);
        termTitle = findViewById(R.id.termTitle);
        startDateTerm = findViewById(R.id.startDateTerm);
        endDateTerm = findViewById(R.id.endDateTerm);
        courseSpinner = findViewById(R.id.courseSpinner);
        termAddCourseBtn = findViewById(R.id.termAddCourseBtn);
        termRemoveCourseBtn = findViewById(R.id.termRemoveCourseBtn);
        addTermSaveBtn = findViewById(R.id.addTermSaveBtn);
        termId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        termTitle.setText(title);

        // Converting database value for start date to string
        Long start = getIntent().getLongExtra("start", -1);
        startDate = new Date(start);
        String startString = format1.format(startDate);
        startDateTerm.setText(startString);

        // Converting database value for end date to string
        Long end = getIntent().getLongExtra("end", -1);
        endDate = new Date(end);
        String endString = format1.format(endDate);
        endDateTerm.setText(endString);

        List<Course> courseList = repository.getCourses();
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
        List<Course> assocCourseList = repository.getAssocTermCourses(termId);
        List<Course> selectedCourseList = new ArrayList<>();
        for (int i = 0; assocCourseList.size() > i; i++) {
            selectedCourseList.add(assocCourseList.get(i));

        }
        RecyclerView associatedCoursesView = findViewById(R.id.coursesTermView);
        associatedCoursesView.setHasFixedSize(true);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        associatedCoursesView.setAdapter(courseAdapter);
        associatedCoursesView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(selectedCourseList);

        startDateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = startDateTerm.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedTerm.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
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
                new DatePickerDialog(DetailedTerm.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
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

        associatedCoursesView.setHasFixedSize(true);
        associatedCoursesView.setAdapter(courseAdapter);
        associatedCoursesView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(selectedCourseList);

        termAddCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCourseList.contains(selectedCourse)) {
                    return;
                } else {
                    selectedCourseList.add(selectedCourse);
                    courseAdapter.setCourses(selectedCourseList);
                    courseAdapter.notifyDataSetChanged();
                }
            }
        });
        termRemoveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; selectedCourseList.size() > i; i++) {
                    if (selectedCourseList.get(i).getCourseId() == selectedCourse.getCourseId()) {
                        selectedCourseList.remove(i);
                        courseAdapter.setCourses(selectedCourseList);
                        courseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        addTermSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int termId = 0;
                if (repository.getTerms().size() == 0) {
                    termId = 1;
                } else {
                    termId = repository.getTerms().get(repository.getTerms().size() - 1).getTermId() + 1;
                }

                String title = termTitle.getText().toString();
                String dateStart = startDateTerm.getText().toString();
                String dateEnd = endDateTerm.getText().toString();
                Date finalStart = null;
                Date finalEnd = null;

                try {
                    finalStart = format1.parse(dateStart);
                    finalEnd = format1.parse(dateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Term term = new Term(termId, title, finalStart, finalEnd);
                repository.update(term);

                for (int i = 0; selectedCourseList.size() > i; i++) {
                    updatingCourseTermId(selectedCourseList.get(i), termId);
                }
                Intent intent = new Intent(DetailedTerm.this, TermList.class);
                startActivity(intent);
            }
        });


    }

    private void updatingCourseTermId(Course course, int termId) {
        int courseId = course.getCourseId();
        String courseTitle = course.getCourseName();
        Date courseStart = course.getCourseStart();
        Date courseEnd = course.getCourseEnd();
        String courseStatus = course.getCourseStatus();
        int instructorId = course.getInstructorId();
        String courseNotes = course.getCourseOptionalNotes();
        Course course1 = new Course(courseId, courseTitle, courseStart, courseEnd, courseStatus, instructorId, courseNotes, termId);
        repository.update(course1);
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