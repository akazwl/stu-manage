package com.example.stumanage.service;

import com.example.stumanage.model.Assignment;
import com.example.stumanage.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> getAssignmentByCourseId(Integer courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    public List<Assignment> getAllAssignment() {
        return assignmentRepository.findAll();
    }

    public Assignment addAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Integer id) {
        assignmentRepository.deleteById(id);
    }
}