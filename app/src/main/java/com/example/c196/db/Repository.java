package com.example.c196.db;

import com.example.c196.DAO.AssessmentDAO;
import com.example.c196.DAO.CourseDAO;
import com.example.c196.DAO.InstructorDAO;
import com.example.c196.DAO.TermDAO;
import com.example.c196.Enitiy.Assessment;
import com.example.c196.Enitiy.Course;
import com.example.c196.Enitiy.Instructor;
import com.example.c196.Enitiy.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private AssessmentDAO mAssessmentDAO;
    private CourseDAO mCourseDAO;
    private InstructorDAO mInstructorDAO;
    private TermDAO mTermDAO;

    private List<Assessment> mAllAssessment;
    private List<Course> mAllCourses;
    private List<Instructor> mAllInstructors;
    private List<Term> mAllTerms;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
