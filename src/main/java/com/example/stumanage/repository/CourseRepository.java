package com.example.stumanage.repository;

import com.example.stumanage.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherId(Integer teacherId);
}