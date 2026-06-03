package com.example.stumanage.repository;

import com.example.stumanage.model.TrainingLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrainingLectureRepository extends JpaRepository<TrainingLecture, Integer> {
    List<TrainingLecture> findByStudentId(Integer studentId);
    List<TrainingLecture> findByStatus(String status);
}
