package com.example.c196.UI;

import static com.example.c196.UI.MainActivity.notificationAlertNumber;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.c196.Entity.Assessment;
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

public class DetailedCourse extends AppCompatActivity {
    Repository repository = new Repository(getApplication());
    DatePickerDialog.OnDateSetListener listenStart;
    DatePickerDialog.OnDateSetListener listenEnd;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);

    EditText courseStartDate, courseEndDate, courseTitle, courseNotes;
    TextView instructNameField, instructPhoneField, instructEmailField;
    Spinner courseStatus, courseInstructor, termCourseSpinner;
    Button courseSaveBtn, newInstructor;
    Date startDate, endDate;
    String title;
    int instructorId, selectedInstructor, courseId, termId, selectedTerm;
    Course updatingCourse;
    Boolean existingCourse;
    ImageView shareNotes;
    View notification;
    private Course course;
    CheckBox checkBoxStart;
    CheckBox checkBoxEnd;
    private TextView emptyView;

    List<Assessment> allAssessments;
    List<Assessment> associatedAssessments;

    /* Creating menu to display the delete button when modifying an existing entry only */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_action_bar, menu);
        if (!existingCourse) {
            menu.findItem(R.id.deleteBtn).setVisible(false);
        }
        return true;
    }

    /* Part of Menu that deletes an existing course when the trashcan icon is selected  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteBtn:
                String status = courseStatus.getSelectedItem().toString();
                String notes = courseNotes.getText().toString();
                Course course = new Course(courseId, title, startDate, endDate, status, selectedInstructor, notes, termId);
                Toast.makeText(this, "Course has been deleted", Toast.LENGTH_LONG).show();
                repository.delete(course);
                Intent intent = new Intent(DetailedCourse.this, CourseList.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course);

        courseTitle = findViewById(R.id.courseTitle);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        courseInstructor = findViewById(R.id.courseInstructor);
        courseNotes = findViewById(R.id.courseNotes);
        shareNotes = findViewById(R.id.shareNotes);
        courseSaveBtn = findViewById(R.id.courseSaveBtn);
        checkBoxStart = findViewById(R.id.checkBoxStart);
        checkBoxEnd = findViewById(R.id.checkBoxEnd);
        newInstructor = findViewById(R.id.newInstructor);
        instructNameField = findViewById(R.id.instructNameField);
        instructPhoneField = findViewById(R.id.instructPhoneField);
        instructEmailField = findViewById(R.id.instructEmailField);
        termCourseSpinner = findViewById(R.id.termCourseSpinner);
        instructorId = getIntent().getIntExtra("instructorId", -1);
        courseNotes = findViewById(R.id.courseNotes);
        courseId = getIntent().getIntExtra("id", -1);
        termId = getIntent().getIntExtra("termId", -1);
        String notes = getIntent().getStringExtra("notes");
        courseNotes.setText(notes);
        title = getIntent().getStringExtra("title");

        /* Spinner to display all Terms in dropdown */
        List<Term> termList = repository.getTerms();
        ArrayAdapter<Term> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, termList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termCourseSpinner.setAdapter(termAdapter);

        termCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTerm = termList.get(i).getTermId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /* Converting database value for start date to string */
        Long start = getIntent().getLongExtra("start", -1);
        startDate = new Date(start);
        String startString = format1.format(startDate);

        /* Converting database value for end date to string */
        Long end = getIntent().getLongExtra("end", -1);
        endDate = new Date(end);
        String endString = format1.format(endDate);

        /* Determining if editing a existing course and setting the fields */
        for (Course course : repository.getCourses()) {
            if (course.getCourseId() == courseId) {
                updatingCourse = course;
            }
        }
        if (updatingCourse != null) {
            existingCourse = true;
            courseTitle.setText(title);
            courseStartDate.setText(startString);
            courseEndDate.setText(endString);
        } else {
            existingCourse = false;
        }

        /* Start Date Listener for date picker */
        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = courseStartDate.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedCourse.this, listenStart, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
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

        /* End Date Listener for date picker */
        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = courseEndDate.getText().toString();
                try {
                    calendarEnd.setTime(format1.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedCourse.this, listenEnd, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
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

        newInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedCourse.this, AddInstructor.class);
                startActivity(intent);
            }
        });

        /* Spinner for Instructors */
        onRestart();
        List<Instructor> instructorList = repository.getInstructors();
        ArrayAdapter<Instructor> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, instructorList);
        courseInstructor.getAdapter();
        courseInstructor.setAdapter(typeAdapter);
        for (int i = 0; instructorList.size() > i; i++) {
            if (instructorList.get(i).getInstructorId() == instructorId) {
                courseInstructor.setSelection(i);
            }
        }
        /* Displaying instructor information for the selected instructor in the spinner */
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseInstructor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = instructorList.get(i).getInstructorName();
                String phone = instructorList.get(i).getInstructorPhone();
                String email = instructorList.get(i).getInstructorEmail();
                instructNameField.setText(name);
                instructPhoneField.setText(phone);
                instructEmailField.setText(email);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /* Array list for getting associated assessments and displaying them in the associatedCoursesView RecyclerView */
        allAssessments = repository.getAssessments();
        associatedAssessments = new ArrayList<>();

        for (Assessment a : allAssessments) {
            if (a.getCourseId() == courseId) {
                associatedAssessments.add(a);
            }
        }
        RecyclerView associatedCoursesView = findViewById(R.id.assessments_courses);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        associatedCoursesView.setAdapter(assessmentAdapter);
        associatedCoursesView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(associatedAssessments);

        /* Displaying a message if no associated assessments exist */
        emptyView = (TextView) findViewById(R.id.empty_view_course);
        if (associatedAssessments.isEmpty()) {
            associatedCoursesView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            associatedCoursesView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        /* Spinner for Course Status */
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.courseStatusString, android.R.layout.simple_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatus.setAdapter(statusAdapter);
        String currentStatus = getIntent().getStringExtra("type");
        int intStatus = statusAdapter.getPosition(currentStatus);
        courseStatus.setSelection(intStatus);

        /* Icon to click on for sharing notes. If no text is present in the Notes field - an error
        message will display */
        shareNotes.setOnClickListener(view -> {
            String sharingNotes = courseNotes.getText().toString();
            if (courseNotes.getText().toString().isEmpty()) {
                Toast.makeText(this, "No notes available to share. Please add note and try again.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingNotes);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        /* Save button listener. */
        courseSaveBtn.setOnClickListener(view -> {
            String title = courseTitle.getText().toString();
            String dateStart = courseStartDate.getText().toString();
            String dateEnd = courseEndDate.getText().toString();
            Date finalStart = null;
            Date finalEnd = null;
            String status = courseStatus.getSelectedItem().toString();
            String notes1 = courseNotes.getText().toString();
            try {
                finalStart = format1.parse(dateStart);
                finalEnd = format1.parse(dateEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (termList.size() == 0) {
                Toast.makeText(this, "Please add a term first before creating a course.", Toast.LENGTH_LONG).show();
                return;
            }
            if (courseTitle.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a course title.", Toast.LENGTH_LONG).show();
                return;
            }
            if (finalStart == null || finalEnd == null) {
                Toast.makeText(this, "Please check your start and end date", Toast.LENGTH_LONG).show();
                return;
            }
            if (courseInstructor == null) {
                Toast.makeText(this, "Please add an instructor.", Toast.LENGTH_LONG).show();
                return;
            }
            if (repository.getCourses().size() == 0) {
                courseId = 1;
                Course course = new Course(courseId, title, finalStart, finalEnd, status, selectedInstructor, notes1, selectedTerm);
                repository.insert(course);
            } else if (courseId != -1) {
                Course course = new Course(courseId, title, finalStart, finalEnd, status, selectedInstructor, notes1, selectedTerm);
                if (checkBoxStart.isChecked()) {
                    startNotification();
                }
                if (checkBoxEnd.isChecked()) {
                    endNotification();
                }
                repository.update(course);
            } else {
                courseId = repository.getCourses().get(repository.getCourses().size() - 1).getCourseId() + 1;
                Course course = new Course(courseId, title, finalStart, finalEnd, status, selectedInstructor, notes1, selectedTerm);
                repository.insert(course);
            }
            Intent intent = new Intent(DetailedCourse.this, CourseList.class);
            startActivity(intent);
        });
    }

    /* Reloading instructor spinner data when new instructor is added */
    public void onRestart() {
        super.onRestart();
        List<Instructor> instructorList = repository.getInstructors();
        ArrayAdapter<Instructor> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, instructorList);
        courseInstructor.getAdapter();
        courseInstructor.setAdapter(typeAdapter);
    }

    /* Method to update labels for start and end date */
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

    /* Notification alert for start date on course */
    private void startNotification() {
        String dateFromScreen = courseStartDate.getText().toString();
        Date notificationActivity = null;
        try {
            notificationActivity = format1.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = notificationActivity.getTime();
        Intent intentNotification = new Intent(DetailedCourse.this, MyReceiver.class);
        intentNotification.putExtra("key", "Course: " + courseTitle.getText().toString() + " starts today");
        PendingIntent sender = PendingIntent.getBroadcast(DetailedCourse.this, MainActivity.notificationAlertNumber++, intentNotification, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    /* Notification alert for end date on course */
    private void endNotification() {
        String dateFromScreen = courseEndDate.getText().toString();
        Date notificationActivity = null;
        try {
            notificationActivity = format1.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = notificationActivity.getTime();
        Intent intentNotification = new Intent(DetailedCourse.this, MyReceiver.class);
        intentNotification.putExtra("key", "Course: " + courseTitle.getText().toString() + " ends today");
        PendingIntent sender = PendingIntent.getBroadcast(DetailedCourse.this, MainActivity.notificationAlertNumber++, intentNotification, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }
}