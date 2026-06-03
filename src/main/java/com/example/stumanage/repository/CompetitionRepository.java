package com.example.stumanage.repository;

import com.example.stumanage.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    List<Competition> findByStudentId(Integer studentId);
    List<Competition> findByStatus(String status);
}
