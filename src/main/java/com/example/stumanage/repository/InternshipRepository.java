package com.example.stumanage.repository;

import com.example.stumanage.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Integer> {
    List<Internship> findByStudentId(Integer studentId);
    List<Internship> findByStatus(String status);
}
