package com.example.c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.c196.Entity.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY courseId ASC")
    List<Course> getCourses();

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> getAssocTermCourses(int termId);

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    List<Course> getAssocCourses(int courseId);

    @Query("SELECT courses.*, terms.termName as term_title, terms.termStart as term_start_date, terms.termEnd as term_end_date FROM courses JOIN terms ON courses.termId = terms.termId ORDER BY terms.termStart, courses.courseStart")
    List<Course> getAllCourseTerms();
}
