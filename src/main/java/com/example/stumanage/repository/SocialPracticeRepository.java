package com.example.stumanage.repository;

import com.example.stumanage.model.SocialPractice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SocialPracticeRepository extends JpaRepository<SocialPractice, Integer> {
    List<SocialPractice> findByStudentId(Integer studentId);
    List<SocialPractice> findByStatus(String status);
}
