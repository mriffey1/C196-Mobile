package com.example.c196.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196.Entity.Assessment;
import com.example.c196.Entity.Course;
import com.example.c196.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<com.example.c196.UI.CourseAdapter.CourseViewHolder> {

        class CourseViewHolder extends RecyclerView.ViewHolder {
            private final TextView courseItemView;

            private CourseViewHolder(View itemView) {
                super(itemView);
                courseItemView = itemView.findViewById(R.id.textView2);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        final Course current = mCourses.get(position);
                        Intent intent = new Intent(context, DetailedCourse.class);
                        intent.putExtra("id", current.getCourseId());
                        intent.putExtra("title", current.getCourseName());
                        intent.putExtra("start", current.getCourseStart());
                        intent.putExtra("end", current.getCourseEnd());
                        intent.putExtra("type", current.getCourseStatus());
                        intent.putExtra("notes", current.getCourseOptionalNotes());
                        intent.putExtra("iid", current.getInstructorId());
                        intent.putExtra("tid", current.getTermId());
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
                Course current = mCourses.get(position);
                String title = current.getCourseName();
                holder.courseItemView.setText(title);
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



