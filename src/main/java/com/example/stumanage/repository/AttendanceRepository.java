package com.example.stumanage.repository;

import com.example.stumanage.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByStudentId(Integer studentId);
    List<Attendance> findByCourseId(Integer courseId);
    List<Attendance> findByCourseIdAndStudentId(Integer courseId, Integer studentId);
}