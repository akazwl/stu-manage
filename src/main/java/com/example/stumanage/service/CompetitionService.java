package com.example.stumanage.service;

import com.example.stumanage.model.Competition;
import com.example.stumanage.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    //=======学生端=======
    public List<Competition> getByStudent(Integer studentId) {
        return competitionRepository.findByStudentId(studentId);
    }

    public Competition add(Competition competition) {
        competition.setStatus("PENDING");
        return competitionRepository.save(competition);
    }

    public void delete(Integer id) {
        competitionRepository.deleteById(id);
    }

    //=======教师端=======
    public List<Competition> getAllPending() {
        return competitionRepository.findByStatus("PENDING");
    }

    public Competition approve(Integer id, String status, String comment) {
        Competition competition = competitionRepository.findById(id).orElse(null);
        if (competition != null && "PENDING".equals(competition.getStatus())) {
            competition.setStatus(status);
            competition.setTeacherComment(comment);
            return competitionRepository.save(competition);
        }
        return null;
    }
}
