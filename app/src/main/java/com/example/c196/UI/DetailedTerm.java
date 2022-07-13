package com.example.c196.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Term updatingTerm;
    Button deleteBtn;
    Term term;
    Menu menu;
    boolean existingTerm = false;
    boolean value;

    /* Creating menu to display the delete button when modifying an existing entry only */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_action_bar, menu);
        if (!existingTerm) {
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
                int count = 0;
                for (Course course : repository.getCourses()) {
                    if (course.getTermId() == termId) {
                        ++count;
                    }
                }
                if (count == 0) {
                    Term term = new Term(termId, title, startDate, endDate);
                    Toast.makeText(this, "Term has been deleted", Toast.LENGTH_LONG).show();
                    repository.delete(term);
                    Intent intent = new Intent(DetailedTerm.this, TermList.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Unable to delete. Term has assigned courses. Please remove courses, save, and try again.", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteBtn = findViewById(R.id.deleteBtn);
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


        /* Converting database value for start date to a string */
        Long start = getIntent().getLongExtra("start", -1);
        startDate = new Date(start);
        String startString = format1.format(startDate);

        /* Converting database value for end date to a string */
        Long end = getIntent().getLongExtra("end", -1);
        endDate = new Date(end);
        String endString = format1.format(endDate);

        /* Determining whether the item clicked from UI.TermList is a new term or an existing item.
         * If it is an existing item - the appropriate fields will display the information.  */
        for (Term term : repository.getTerms()) {
            if (term.getTermId() == termId) {
                updatingTerm = term;
            }
        }
        if (updatingTerm != null) {
            existingTerm = true;
            termTitle.setText(title);
            startDateTerm.setText(startString);
            endDateTerm.setText(endString);
        }
        /* Start Date Listener for date picker */
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
        /* End Date Listener for date picker */
        endDateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info2 = endDateTerm.getText().toString();
                try {
                    calendarStart.setTime((Date) format1.parse(info2));
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

//        /* Adapter for available courses to set spinner */
//        List<Course> courseList = repository.getCourses();
//        if (!courseList.isEmpty()){
//            ArrayAdapter<Course> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseList);
//            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            courseSpinner.setAdapter(typeAdapter);
//        } else {
//            courseSpinner.setSelection(0);
//        }


//        /* Identifies the selected course from the spinner from the courseList */
//        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedCourse = courseList.get(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });

        /* Sets lists to create an associated course list when added to the recycle view. */
        List<Course> assocCourseList = repository.getAssocTermCourses(termId);
        RecyclerView associatedCoursesView = findViewById(R.id.coursesTermView);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        associatedCoursesView.setAdapter(courseAdapter);
        associatedCoursesView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(assocCourseList);
//
//        /* Add Course button that determines whether the selected course is already present and if not,
//         * then adds the course to the selectedCourseList */
//        termAddCourseBtn.setOnClickListener(view -> {
//            if (selectedCourseList.contains(selectedCourse)) {
//                Toast.makeText(this, "Course already added.", Toast.LENGTH_LONG).show();
//                return;
//            } else {
//                selectedCourseList.add(selectedCourse);
//                courseAdapter.setCourses(selectedCourseList);
//                Toast.makeText(this, "Course added.", Toast.LENGTH_LONG).show();
//                courseAdapter.notifyDataSetChanged();
//            }
//        });
//
//        /* Remove Course button that determines whether the selected course is on the selectedCourseList,
//         * and then removes the course from the list */
//        termRemoveCourseBtn.setOnClickListener(view -> {
//            for (int i = 0; selectedCourseList.size() > i; i++) {
//                if (selectedCourseList.get(i).getCourseId() == selectedCourse.getCourseId()) {
//                    int id = selectedCourseList.get(i).getCourseId();
//                    selectedCourseList.remove(i);
//                    courseAdapter.setCourses(selectedCourseList);
//
//                    Toast.makeText(this, "Course removed.", Toast.LENGTH_LONG).show();
//                    courseAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(this, "Course is not associated with term.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        /* Save button listener. On save, courses removed from the associated list will have their ID's updated to zero.
         * Also updates associated course term ID's, and determines whether this is a new term or existing term and updates
         * the termId appropriately.  */
        addTermSaveBtn.setOnClickListener(view -> {

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



            if (repository.getTerms().size() == 0){
                termId = 1;
                Term term = new Term(termId, title, finalStart, finalEnd);
                repository.insert(term);
            } else if (termId != -1) {
                Term term = new Term(termId, title, finalStart, finalEnd);
                repository.update(term);
            } else {
                termId = repository.getTerms().get(repository.getTerms().size() - 1).getTermId() + 1;
                updatingCourseTermId(selectedCourse, termId);
                Term term = new Term(termId, title, finalStart, finalEnd);
                repository.insert(term);
            }
            Intent intent = new Intent(DetailedTerm.this, TermList.class);
            startActivity(intent);
        });
    }

    /* Method to update the course termId */
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

    /* Method to update labels for start and end dates */
    private void updateLabel(boolean value) {
        if (value) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            startDateTerm.setText(format1.format(calendarStart.getTime()));
        } else {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            endDateTerm.setText(format1.format(calendarEnd.getTime()));
        }
    }
}