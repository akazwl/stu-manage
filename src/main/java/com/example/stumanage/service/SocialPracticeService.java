package com.example.stumanage.service;

import com.example.stumanage.model.SocialPractice;
import com.example.stumanage.repository.SocialPracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SocialPracticeService {

    @Autowired
    private SocialPracticeRepository socialPracticeRepository;

    //=======学生端=======
    public List<SocialPractice> getByStudent(Integer studentId) {
        return socialPracticeRepository.findByStudentId(studentId);
    }

    public SocialPractice add(SocialPractice socialPractice) {
        socialPractice.setStatus("PENDING");
        return socialPracticeRepository.save(socialPractice);
    }

    public void delete(Integer id) {
        socialPracticeRepository.deleteById(id);
    }

    //=======教师端=======
    public List<SocialPractice> getAllPending() {
        return socialPracticeRepository.findByStatus("PENDING");
    }

    public SocialPractice approve(Integer id, String status, String comment) {
        SocialPractice socialPractice = socialPracticeRepository.findById(id).orElse(null);
        if (socialPractice != null && "PENDING".equals(socialPractice.getStatus())) {
            socialPractice.setStatus(status);
            socialPractice.setTeacherComment(comment);
            return socialPracticeRepository.save(socialPractice);
        }
        return null;
    }
}
