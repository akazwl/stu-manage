package com.example.stumanage.service;

import com.example.stumanage.model.AssignmentSubmission;
import com.example.stumanage.repository.AssignmentSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    public List<AssignmentSubmission> getAssignmentSubmissionByStudentId(Integer studentId) {
        return assignmentSubmissionRepository.findByStudentId(studentId);
    }

    public List<AssignmentSubmission> getAssignmentSubmissionByAssignmentId(Integer assignmentId) {
        return assignmentSubmissionRepository.findByAssignmentId(assignmentId);
    }

    public List<AssignmentSubmission> getAllAssignmentSubmission() {
        return assignmentSubmissionRepository.findAll();
    }

    public AssignmentSubmission addAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
        return assignmentSubmissionRepository.save(assignmentSubmission);
    }

    public AssignmentSubmission updateAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
        return assignmentSubmissionRepository.save(assignmentSubmission);
    }

    public void deleteAssignmentSubmission(Integer id) {
        assignmentSubmissionRepository.deleteById(id);
    }
}