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

import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Course;
import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedAssessments extends AppCompatActivity {
    Repository repository = new Repository(getApplication());
    DatePickerDialog.OnDateSetListener listenStart;
    DatePickerDialog.OnDateSetListener listenEnd;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
    EditText textTitle;
    EditText startDateAssess;
    EditText endDateAssess;
    Spinner assessmentType;
    int assessmentId;
    int assessmentCourseId;
    Spinner assessmentCourseSpinner;
    Button saveBtn;
    Date startDate;
    Date endDate;
    String title;
    Assessment updatingAssessment;
    int selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessments);
        textTitle = findViewById(R.id.textTitle);
        startDateAssess = findViewById(R.id.startDateAssess);
        assessmentCourseSpinner = findViewById(R.id.assessmentCourseSpinner);
        endDateAssess = findViewById(R.id.endDateAssess);
        assessmentType = findViewById(R.id.assessmentType);
        saveBtn = findViewById(R.id.saveBtn);
        assessmentId = getIntent().getIntExtra("id", -1);
        assessmentCourseId = getIntent().getIntExtra("cid", -1);
        title = getIntent().getStringExtra("title");
        // Converting database value for start date to string
        Long start = getIntent().getLongExtra("start", -1);
        startDate = new Date(start);
        String startString = format1.format(startDate);

        // Spinner for Courses
        List<Course> courseList = repository.getCourses();
        ArrayAdapter<Course> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentCourseSpinner.setAdapter(termAdapter);

        assessmentCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCourse = courseList.get(i).getCourseId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        List<Course> assocCourseList = repository.getAssocCourses(assessmentCourseId);
        System.out.println(assessmentCourseId);
        RecyclerView associatedAssessmentCoursesView = findViewById(R.id.recyclerViewAssessment);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        associatedAssessmentCoursesView.setAdapter(courseAdapter);
        associatedAssessmentCoursesView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(assocCourseList);
        System.out.println(assocCourseList);

        // Converting database value for end date to string
        Long end = getIntent().getLongExtra("end", -1);
        endDate = new Date(end);
        String endString = format1.format(endDate);

        for (Assessment assessment : repository.getAssessments()) {
            if (assessment.getAssessmentId() == assessmentId) {
                updatingAssessment = assessment;
            }
        }
        if (updatingAssessment != null) {
            textTitle.setText(title);
            startDateAssess.setText(startString);
            endDateAssess.setText(endString);
        }
        startDateAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = startDateAssess.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedAssessments.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
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

        endDateAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = endDateAssess.getText().toString();
                try {
                    calendarEnd.setTime(format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedAssessments.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
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
        Spinner typeSpinner = (Spinner) findViewById(R.id.assessmentType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.addAssessType, android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        String currentStatus = getIntent().getStringExtra("type");
        int intStatus = typeAdapter.getPosition(currentStatus);
        assessmentType.setSelection(intStatus);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = textTitle.getText().toString();
                String type = typeSpinner.getSelectedItem().toString();

                String dateStart = startDateAssess.getText().toString();
                String dateEnd = endDateAssess.getText().toString();
                Date finalStart = null;
                Date finalEnd = null;
                try {
                    finalStart = format1.parse(dateStart);
                    finalEnd = format1.parse(dateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (repository.getAssessments().size() == 0) {
                    assessmentId = 1;
                    Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                    repository.insert(assessment);
                } else if (assessmentId != -1){
                    Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                    repository.update(assessment);
                } else {
                    assessmentId = repository.getAssessments().get(repository.getAssessments().size() - 1).getAssessmentId() + 1;
                    Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                    repository.insert(assessment);
                }

                Intent intent = new Intent(DetailedAssessments.this, AssessmentsList.class);
                startActivity(intent);

            }
        });

    }

    // Method to update labels for start and end date
    private void updateLabel(boolean value) {
        if (value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            startDateAssess.setText(format1.format(calendarStart.getTime()));
        } else if (!value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            endDateAssess.setText(format1.format(calendarEnd.getTime()));
        }
    }
}