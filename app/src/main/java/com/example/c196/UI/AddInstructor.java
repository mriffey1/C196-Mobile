package com.example.c196.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.c196.Entity.Instructor;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.io.Serializable;
import java.util.List;

import javax.xml.transform.Result;

public class AddInstructor extends AppCompatActivity {
    EditText instructName;
    EditText instructPhone;
    EditText instructEmail;
    Button instructSaveBtn;
    private final Repository repository = new Repository(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor);
        instructName = findViewById(R.id.instructName);
        instructPhone = findViewById(R.id.instructPhone);
        instructEmail = findViewById(R.id.instructEmail);
        instructSaveBtn = findViewById(R.id.instructSaveBtn);

        instructSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int instructorId = 0;
                if (repository.getInstructors().size() == 0) {
                    instructorId = 1;
                } else {
                    instructorId = repository.getInstructors().get(repository.getInstructors().size() - 1).getInstructorId() + 1;
                }
                String name = instructName.getText().toString();
                String phone = instructPhone.getText().toString();
                String email = instructEmail.getText().toString();

                Instructor instructor = new Instructor(instructorId, name, phone, email);
                repository.insert(instructor);

                Intent intent = new Intent(AddInstructor.this, DetailedCourse.class);
                finish();
                recreate();
                //  startActivity(intent);
            }

            private void finishActivity(Intent intent) {
            }


        });
    }
}
