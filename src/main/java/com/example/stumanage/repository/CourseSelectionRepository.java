package com.example.stumanage.repository;

import com.example.stumanage.model.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Integer> {
    List<CourseSelection> findByStudentId(Integer studentId);
    List<CourseSelection> findByCourseId(Integer courseId);
}