package com.example.stumanage.repository;

import com.example.stumanage.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Integer> {
    List<AssignmentSubmission> findByStudentId(Integer studentId);
    List<AssignmentSubmission> findByAssignmentId(Integer assignmentId);
}