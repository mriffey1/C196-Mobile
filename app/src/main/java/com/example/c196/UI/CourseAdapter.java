package com.example.c196.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196.Entity.Course;
import com.example.c196.Entity.Term;
import com.example.c196.R;
import com.example.c196.db.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<com.example.c196.UI.CourseAdapter.CourseViewHolder> {
    Term term;


    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView, courseDateList, courseEndDateList, courseTermName, courseStatus;



        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.course_name);
            courseTermName = itemView.findViewById(R.id.term_name);
            courseDateList = itemView.findViewById(R.id.termStartDatesList);
            courseEndDateList = itemView.findViewById(R.id.courseEndDateList);
            courseStatus = itemView.findViewById(R.id.courseStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, DetailedCourse.class);
                    intent.putExtra("id", current.getCourseId());
                    intent.putExtra("title", current.getCourseName());
                    intent.putExtra("start", current.getCourseStart().getTime());
                    intent.putExtra("end", current.getCourseEnd().getTime());
                    intent.putExtra("type", current.getCourseStatus());
                    intent.putExtra("notes", current.getCourseOptionalNotes());
                    intent.putExtra("instructorId", current.getInstructorId());
                    intent.putExtra("termId", current.getTermId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if (mCourses != null) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            Course current = mCourses.get(position);
            String courseListTitle = current.getCourseName();
            String status = current.getCourseStatus();
            Date start = current.getCourseStart();
            Date end = current.getCourseEnd();
            String startString = "Start Date: " + format1.format(start);
            String endString = "End Date: " + format1.format(end);

            String date = startString + " - " + endString;
            holder.courseItemView.setText(courseListTitle);
            holder.courseStatus.setText(status);
            holder.courseDateList.setText(startString);
            holder.courseEndDateList.setText(endString);

        } else {
            holder.courseItemView.setText("No title");
        }
    }


    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        } else {
            return 0;
        }
    }
}



