package com.example.c196.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196.Entity.Instructor;
import com.example.c196.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {
    class InstructorViewHolder extends RecyclerView.ViewHolder {
        private final TextView instructor_name;
        private final TextView instructorPhoneNum;
        private final TextView instructorEmailAdd;

        private InstructorViewHolder(View itemView) {
            super(itemView);
            instructor_name = itemView.findViewById(R.id.instructor_name);
            instructorPhoneNum = itemView.findViewById(R.id.instructorPhoneNum);
            instructorEmailAdd = itemView.findViewById(R.id.instructorEmailAdd);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Instructor current = mInstructors.get(position);
                    Intent intent = new Intent(context, AddInstructor.class);
                    intent.putExtra("id", current.getInstructorId());
                    intent.putExtra("name", current.getInstructorName());
                    intent.putExtra("phone", current.getInstructorPhone());
                    intent.putExtra("email", current.getInstructorEmail());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Instructor> mInstructors;
    private final Context context;
    private final LayoutInflater mInflater;

    public InstructorAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public InstructorAdapter.InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.instructor_list_item, parent, false);
        return new InstructorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAdapter.InstructorViewHolder holder, int position) {
        if (mInstructors != null) {
            Instructor current = mInstructors.get(position);
            String instructName = current.getInstructorName();
            String phone = current.getInstructorPhone();
            String email = current.getInstructorEmail();

            holder.instructor_name.setText(instructName);
            holder.instructorPhoneNum.setText(phone);
            holder.instructorEmailAdd.setText(email);

        } else {
            holder.instructor_name.setText("No title");
        }
    }


    public void setInstructors(List<Instructor> instructors) {
        mInstructors = instructors;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mInstructors != null) {
            return mInstructors.size();
        } else {
            return 0;
        }
    }
}