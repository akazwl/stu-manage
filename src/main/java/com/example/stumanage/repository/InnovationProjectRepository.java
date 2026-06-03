package com.example.stumanage.repository;

import com.example.stumanage.model.InnovationProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InnovationProjectRepository extends JpaRepository<InnovationProject, Integer> {
    List<InnovationProject> findByStudentId(Integer studentId);
    List<InnovationProject> findByStatus(String status);
}
