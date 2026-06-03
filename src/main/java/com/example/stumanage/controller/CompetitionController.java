package com.example.stumanage.controller;

import com.example.stumanage.model.Competition;
import com.example.stumanage.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition")
@CrossOrigin(origins = "*")
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<Competition> getMyList(@RequestParam Integer studentId) {
        return competitionService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public Competition add(@RequestBody Competition competition) {
        return competitionService.add(competition);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        competitionService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<Competition> getPendingList() {
        return competitionService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public Competition approve(@PathVariable Integer id,
                               @RequestParam String status,
                               @RequestParam String comment) {
        return competitionService.approve(id, status, comment);
    }
}
