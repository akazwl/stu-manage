package com.example.stumanage.controller;

import com.example.stumanage.model.ScienceAchievement;
import com.example.stumanage.service.ScienceAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/science-achievement")
@CrossOrigin(origins = "*")
public class ScienceAchievementController {

    @Autowired
    private ScienceAchievementService scienceAchievementService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<ScienceAchievement> getMyList(@RequestParam Integer studentId) {
        return scienceAchievementService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public ScienceAchievement add(@RequestBody ScienceAchievement achievement) {
        return scienceAchievementService.add(achievement);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        scienceAchievementService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<ScienceAchievement> getPendingList() {
        return scienceAchievementService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public ScienceAchievement approve(@PathVariable Integer id,
                                      @RequestParam String status,
                                      @RequestParam String comment) {
        return scienceAchievementService.approve(id, status, comment);
    }
}
