package com.example.stumanage.service;

import com.example.stumanage.model.ScienceAchievement;
import com.example.stumanage.repository.ScienceAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScienceAchievementService {

    @Autowired
    private ScienceAchievementRepository scienceAchievementRepository;

    //=======学生端=======
    public List<ScienceAchievement> getByStudent(Integer studentId) {
        return scienceAchievementRepository.findByStudentId(studentId);
    }

    public ScienceAchievement add(ScienceAchievement achievement) {
        achievement.setStatus("PENDING");
        return scienceAchievementRepository.save(achievement);
    }

    public void delete(Integer id) {
        scienceAchievementRepository.deleteById(id);
    }

    //=======教师端=======
    public List<ScienceAchievement> getAllPending() {
        return scienceAchievementRepository.findByStatus("PENDING");
    }

    public ScienceAchievement approve(Integer id, String status, String comment) {
        ScienceAchievement achievement = scienceAchievementRepository.findById(id).orElse(null);
        if (achievement != null && "PENDING".equals(achievement.getStatus())) {
            achievement.setStatus(status);
            achievement.setTeacherComment(comment);
            return scienceAchievementRepository.save(achievement);
        }
        return null;
    }
}
