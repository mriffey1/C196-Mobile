package com.example.c196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Course;

import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    EditText textTitle, startDateAssess, endDateAssess;
    Spinner assessmentType, assessmentCourseSpinner;
    String type, title;
    int assessmentId, assessmentCourseId, selectedCourse;
    Button saveBtn, deleteBtn, assessStartBtn, assessEndBtn;
    Date startDate, endDate;
    Assessment updatingAssessment;
    CheckBox assessStartCheckBtn;
    CheckBox assessEndCheckBtn;

    boolean existingAssessment = false;
    private TextView emptyView;

    List<Course> allCourses;
    List<Course> associatedCourses;


    /* Creating menu to display the delete button when modifying an existing entry only */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_action_bar, menu);
        if (!existingAssessment) {
            menu.findItem(R.id.deleteBtn).setVisible(false);
        }
        return true;
    }

    /* Determining if courses are associated, deleting (or not deleting) the terms and displaying the
     * appropriate messages to the user.  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteBtn:
                Assessment assessment = new Assessment(assessmentId, title, startDate, endDate, type, selectedCourse);
                Toast.makeText(this, "Assessment has been deleted", Toast.LENGTH_LONG).show();
                repository.delete(assessment);
                Intent intent = new Intent(DetailedAssessments.this, AssessmentsList.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessments);
        deleteBtn = findViewById(R.id.deleteBtn);
        textTitle = findViewById(R.id.textTitle);
        startDateAssess = findViewById(R.id.startDateAssess);
        assessmentCourseSpinner = findViewById(R.id.assessmentCourseSpinner);
        endDateAssess = findViewById(R.id.endDateAssess);
        assessmentType = findViewById(R.id.assessmentType);
        saveBtn = findViewById(R.id.saveBtn);
        assessStartBtn = findViewById(R.id.assessStartBtn);
        assessEndBtn = findViewById(R.id.assessEndBtn);
        assessmentId = getIntent().getIntExtra("id", -1);
        assessmentCourseId = getIntent().getIntExtra("cid", -1);
        title = getIntent().getStringExtra("title");

        /* Converting database value for start date to string */
        Long start = getIntent().getLongExtra("start", -1);
        startDate = new Date(start);
        String startString = format1.format(startDate);

        /* Spinner for Courses */
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
        for (int i = 0; i < termAdapter.getCount(); i++) {
            if (courseList.get(i).getCourseId() == assessmentCourseId) {
                assessmentCourseSpinner.setSelection(i);
                break;
            }
        }

        /* Array list for getting associated courses and displaying them in the
        associatedAssessmentCoursesView RecyclerView */
        allCourses = repository.getCourses();
        associatedCourses = new ArrayList<>();
        for (Course c : allCourses) {
            if (c.getCourseId() == assessmentCourseId) {
                associatedCourses.add(c);
            }
        }
        RecyclerView associatedAssessmentCoursesView = findViewById(R.id.recyclerViewAssessment);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        associatedAssessmentCoursesView.setAdapter(courseAdapter);
        associatedAssessmentCoursesView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(associatedCourses);

        /* Converting database value for end date to string */
        Long end = getIntent().getLongExtra("end", -1);
        endDate = new Date(end);
        String endString = format1.format(endDate);

        for (Assessment assessment : repository.getAssessments()) {
            if (assessment.getAssessmentId() == assessmentId) {
                updatingAssessment = assessment;
            }
        }
        if (updatingAssessment != null) {
            existingAssessment = true;
            textTitle.setText(title);
            startDateAssess.setText(startString);
            endDateAssess.setText(endString);
            getSupportActionBar().setTitle("Edit Assessment");
        } else {
            getSupportActionBar().setTitle("Add Assessment");
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

        /* Spinner for assessment types and setting position if an existing assessment */
        Spinner typeSpinner = (Spinner) findViewById(R.id.assessmentType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.addAssessType, android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        String currentStatus = getIntent().getStringExtra("type");
        int intStatus = typeAdapter.getPosition(currentStatus);
        assessmentType.setSelection(intStatus);

        assessStartBtn.setOnClickListener(view -> {
            String dateStart = startDateAssess.getText().toString();
            Date finalStart = null;
            try {
                finalStart = format1.parse(dateStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (finalStart == null) {
                Toast.makeText(this, "Please enter a valid start date first.", Toast.LENGTH_LONG).show();
                return;
            } else {
                startNotification();
                Toast.makeText(this, "Alert for assessment start date has been set.", Toast.LENGTH_LONG).show();
            }
        });
        assessEndBtn.setOnClickListener(view -> {
            String dateEnd = endDateAssess.getText().toString();
            Date finalEnd = null;
            try {
                finalEnd = format1.parse(dateEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (finalEnd == null) {
                Toast.makeText(this, "Please enter a valid end date first.", Toast.LENGTH_LONG).show();
                return;
            } else {
                endNotification();
                Toast.makeText(this, "Alert for assessment end date has been set.", Toast.LENGTH_LONG).show();
            }
        });
        /* Save button */
        saveBtn.setOnClickListener(view -> {
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
            if (courseList.size() == 0) {
                Toast.makeText(this, "Please create a course before creating an assessment.", Toast.LENGTH_LONG).show();
                return;
            }
            if (textTitle.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please add a assessment title.", Toast.LENGTH_LONG).show();
                return;
            }
            if (finalStart == null || finalEnd == null) {
                Toast.makeText(this, "Please check your start and end date.", Toast.LENGTH_LONG).show();
                return;
            }
            if (repository.getAssessments().size() == 0) {
                assessmentId = 1;
                Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                Toast.makeText(this, "Assessment has been added.", Toast.LENGTH_LONG).show();
                repository.insert(assessment);
            } else if (assessmentId != -1) {
                Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                Toast.makeText(this, "Assessment has been updated.", Toast.LENGTH_LONG).show();
                repository.update(assessment);
            } else {
                assessmentId = repository.getAssessments().get(repository.getAssessments().size() - 1).getAssessmentId() + 1;
                Assessment assessment = new Assessment(assessmentId, title, finalStart, finalEnd, type, selectedCourse);
                Toast.makeText(this, "Assessment has been added.", Toast.LENGTH_LONG).show();
                repository.insert(assessment);
            }
            Intent intent = new Intent(DetailedAssessments.this, AssessmentsList.class);
            startActivity(intent);
        });
    }

    /* Method to update labels for start and end date */
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

    /* Notification alert for start date on course */
    private void startNotification() {
        String dateFromScreen = startDateAssess.getText().toString();
        Date notificationActivity = null;
        try {
            notificationActivity = format1.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = notificationActivity.getTime();
        Intent intentNotification = new Intent(DetailedAssessments.this, MyReceiver.class);
        intentNotification.putExtra("key", "Assessment: " + textTitle.getText().toString() + " starts today");
        PendingIntent sender = PendingIntent.getBroadcast(DetailedAssessments.this, MainActivity.notificationAlertNumber++, intentNotification, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    /* Notification alert for end date on course */
    private void endNotification() {
        String dateFromScreen = endDateAssess.getText().toString();
        Date notificationActivity = null;
        try {
            notificationActivity = format1.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = notificationActivity.getTime();
        Intent intentNotification = new Intent(DetailedAssessments.this, MyReceiver.class);
        intentNotification.putExtra("key", "Assessment: " + textTitle.getText().toString() + " ends today");
        PendingIntent sender = PendingIntent.getBroadcast(DetailedAssessments.this, MainActivity.notificationAlertNumber++, intentNotification, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }
}