package com.example.c196.Enitiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termName;
    private Date termStart;
    private Date termEnd;

    public Term(int termId, String termName, Date termStart, Date termEnd) {
        this.termId = termId;
        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public void setTermStart(Date termStart) {
        this.termStart = termStart;
    }

    public void setTermEnd(Date termEnd) {
        this.termEnd = termEnd;
    }

    public int getTermId() {
        return termId;
    }

    public String getTermName() {
        return termName;
    }

    public Date getTermStart() {
        return termStart;
    }

    public Date getTermEnd() {
        return termEnd;
    }
}
