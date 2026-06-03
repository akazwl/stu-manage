package com.example.stumanage.repository;

import com.example.stumanage.model.ScienceAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScienceAchievementRepository extends JpaRepository<ScienceAchievement, Integer> {
    List<ScienceAchievement> findByStudentId(Integer studentId);
    List<ScienceAchievement> findByStatus(String status);
}
