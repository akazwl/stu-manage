package com.example.stumanage.service;

import com.example.stumanage.model.Internship;
import com.example.stumanage.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    //=======学生端=======
    public List<Internship> getByStudent(Integer studentId) {
        return internshipRepository.findByStudentId(studentId);
    }

    public Internship add(Internship internship) {
        internship.setStatus("PENDING");
        return internshipRepository.save(internship);
    }

    public void delete(Integer id) {
        internshipRepository.deleteById(id);
    }

    //=======教师端=======
    public List<Internship> getAllPending() {
        return internshipRepository.findByStatus("PENDING");
    }

    public Internship approve(Integer id, String status, String comment) {
        Internship internship = internshipRepository.findById(id).orElse(null);
        if (internship != null && "PENDING".equals(internship.getStatus())) {
            internship.setStatus(status);
            internship.setTeacherComment(comment);
            return internshipRepository.save(internship);
        }
        return null;
    }
}
