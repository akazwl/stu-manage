package com.example.stumanage.service;

import com.example.stumanage.model.TrainingLecture;
import com.example.stumanage.repository.TrainingLectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingLectureService {

    @Autowired
    private TrainingLectureRepository trainingLectureRepository;

    //=======学生端=======
    public List<TrainingLecture> getByStudent(Integer studentId) {
        return trainingLectureRepository.findByStudentId(studentId);
    }

    public TrainingLecture add(TrainingLecture trainingLecture) {
        trainingLecture.setStatus("PENDING");
        return trainingLectureRepository.save(trainingLecture);
    }

    public void delete(Integer id) {
        trainingLectureRepository.deleteById(id);
    }

    //=======教师端=======
    public List<TrainingLecture> getAllPending() {
        return trainingLectureRepository.findByStatus("PENDING");
    }

    public TrainingLecture approve(Integer id, String status, String comment) {
        TrainingLecture trainingLecture = trainingLectureRepository.findById(id).orElse(null);
        if (trainingLecture != null && "PENDING".equals(trainingLecture.getStatus())) {
            trainingLecture.setStatus(status);
            trainingLecture.setTeacherComment(comment);
            return trainingLectureRepository.save(trainingLecture);
        }
        return null;
    }
}
