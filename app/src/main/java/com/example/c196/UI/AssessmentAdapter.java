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
import com.example.c196.Entity.Term;
import com.example.c196.R;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, DetailedAssessments.class);
                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("title", current.getAssessmentTitle());
                    intent.putExtra("start", current.getAssessmentStart());
                    intent.putExtra("end", current.getAssessmentEnd());
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
            Assessment current = mAssessments.get(position);
            String title = current.getAssessmentTitle();
            holder.assessmentItemView.setText(title);
        } else {
            holder.assessmentItemView.setText("No title");
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


