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
import com.example.c196.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessment_name;
        private final TextView assessment_start_date;
        private final TextView assessment_end_date;
        private final TextView assessmentObjective;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessment_name = itemView.findViewById(R.id.assessment_name);
            assessment_start_date = itemView.findViewById(R.id.assessment_start_date);
            assessment_end_date = itemView.findViewById(R.id.assessment_end_date);
            assessmentObjective = itemView.findViewById(R.id.assessmentObjective);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, DetailedAssessments.class);
                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("title", current.getAssessmentTitle());
                    intent.putExtra("start", current.getAssessmentStart().getTime());
                    intent.putExtra("end", current.getAssessmentEnd().getTime());
                    intent.putExtra("type", current.getAssessmentType());
                    intent.putExtra("cid", current.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            Assessment current = mAssessments.get(position);
            Date start = current.getAssessmentStart();
            Date end = current.getAssessmentEnd();
            String typeAssess = current.getAssessmentType();
            String startString = "Start Date: " + format1.format(start);
            String endString = "End Date: " + format1.format(end);
            String title = current.getAssessmentTitle();
            holder.assessment_name.setText(title);
            holder.assessment_start_date.setText(startString);
            holder.assessment_end_date.setText(endString);
            holder.assessmentObjective.setText(typeAssess);
        } else {
            holder.assessment_name.setText("No title");
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        } else {
            return 0;
        }
    }
}


