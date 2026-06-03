package com.example.stumanage.repository;

import com.example.stumanage.model.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Integer> {
    List<DailyActivity> findByStudentId(Integer studentId);
}
