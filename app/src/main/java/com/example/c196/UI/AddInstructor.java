package com.example.c196.UI;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.c196.Entity.Instructor;
import com.example.c196.R;
import com.example.c196.db.Repository;


public class AddInstructor extends AppCompatActivity {
    private final Repository repository = new Repository(getApplication());
    EditText instructName, instructPhone, instructEmail;
    Button instructSaveBtn;
    int instructorId;
    boolean existingInstructor = false;
    Instructor updatingInstructor;
    String name, email, phone;

    /* Creating menu to display the delete button when modifying an existing entry only */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.term_action_bar, menu);
        if (!existingInstructor) {
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
                Instructor instructor = new Instructor(instructorId, name, phone, email);
                Toast.makeText(this, "Instructor has been deleted", Toast.LENGTH_LONG).show();
                repository.delete(instructor);
                Intent intent = new Intent(AddInstructor.this, InstructorList.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor);
        instructName = findViewById(R.id.instructName);
        instructPhone = findViewById(R.id.instructPhone);
        instructEmail = findViewById(R.id.instructEmail);
        instructSaveBtn = findViewById(R.id.instructSaveBtn);
        instructorId = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");

        for (Instructor instructor : repository.getInstructors()) {
            if (instructor.getInstructorId() == instructorId) {
                updatingInstructor = instructor;
            }
        }
        if (updatingInstructor != null) {
            existingInstructor = true;
            instructName.setText(name);
            instructPhone.setText(phone);
            instructEmail.setText(email);
        }

        instructSaveBtn.setOnClickListener(view -> {
            String name = instructName.getText().toString();
            String phone = instructPhone.getText().toString();
            String email = instructEmail.getText().toString();

            if (instructName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter instructor name.", Toast.LENGTH_LONG).show();
                return;
            }
            if (instructPhone.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter instructor phone number.", Toast.LENGTH_LONG).show();
                return;
            }
            if (instructEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter instructor email.", Toast.LENGTH_LONG).show();
                return;
            }
            System.out.println(instructorId);
            if (repository.getInstructors().size() == 0) {
                instructorId = 1;
                Instructor instructor = new Instructor(instructorId, name, phone, email);
                Toast.makeText(this, "Instructor has been added.", Toast.LENGTH_LONG).show();
                repository.insert(instructor);
            } else if (instructorId != -1) {
                Instructor instructor = new Instructor(instructorId, name, phone, email);
                Toast.makeText(this, "Instructor has been updated.", Toast.LENGTH_LONG).show();
                repository.update(instructor);
            } else {
                instructorId = repository.getInstructors().get(repository.getInstructors().size() - 1).getInstructorId() + 1;
                Instructor instructor = new Instructor(instructorId, name, phone, email);
                Toast.makeText(this, "Instructor has been added.", Toast.LENGTH_LONG).show();
                repository.insert(instructor);
            }

            Intent intent = new Intent(AddInstructor.this, InstructorList.class);
            startActivity(intent);
        });
    }
}
