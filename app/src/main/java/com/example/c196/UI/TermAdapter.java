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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Course> courses = new ArrayList<>();
    private Term term;
    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termTitleList;
        private final TextView termStartDatesList;
        private final TextView termEndDatesList;


        Course courses;
        Date start;

        private TermViewHolder(View itemView) {
            super(itemView);
            termTitleList = itemView.findViewById(R.id.term_name);
            termStartDatesList = itemView.findViewById(R.id.termStartDatesList);
            termEndDatesList = itemView.findViewById(R.id.termEndDatesList);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Term current = mTerms.get(position);
                    Intent intent = new Intent(context, DetailedTerm.class);

                    intent.putExtra("id", current.getTermId());
                    intent.putExtra("title", current.getTermName());
                    intent.putExtra("start", current.getTermStart().getTime());
                    intent.putExtra("end", current.getTermEnd().getTime());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Term> mTerms;
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    public TermAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if (mTerms != null) {
            String format = "MM/dd/yy";
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
            Term current = mTerms.get(position);
            String title = current.getTermName();
            Date start = current.getTermStart();
            Date end = current.getTermEnd();
            String startString = "Start Date: " + format1.format(start);
            String endString = "End Date: " + format1.format(end);
            String date = startString + " - " + endString;

            holder.termTitleList.setText(title);
            holder.termStartDatesList.setText(startString);
            holder.termEndDatesList.setText(endString);

        } else {
            holder.termTitleList.setText("No title");
        }
    }
    public void setCoursesExtended(List<Course> courses, Term term) {
        this.courses = courses;
        this.term = term;
        notifyDataSetChanged();
    }
    public void setTerms(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTerms != null) {
            return mTerms.size();
        } else {
            return 0;
        }
    }
}
