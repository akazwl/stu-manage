package com.example.stumanage.controller;

import com.example.stumanage.model.TrainingLecture;
import com.example.stumanage.service.TrainingLectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-lecture")
@CrossOrigin(origins = "*")
public class TrainingLectureController {

    @Autowired
    private TrainingLectureService trainingLectureService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<TrainingLecture> getMyList(@RequestParam Integer studentId) {
        return trainingLectureService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public TrainingLecture add(@RequestBody TrainingLecture trainingLecture) {
        return trainingLectureService.add(trainingLecture);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        trainingLectureService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<TrainingLecture> getPendingList() {
        return trainingLectureService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public TrainingLecture approve(@PathVariable Integer id,
                                   @RequestParam String status,
                                   @RequestParam String comment) {
        return trainingLectureService.approve(id, status, comment);
    }
}
