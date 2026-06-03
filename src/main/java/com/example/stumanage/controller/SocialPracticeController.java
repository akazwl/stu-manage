package com.example.stumanage.controller;

import com.example.stumanage.model.SocialPractice;
import com.example.stumanage.service.SocialPracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social-practice")
@CrossOrigin(origins = "*")
public class SocialPracticeController {

    @Autowired
    private SocialPracticeService socialPracticeService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<SocialPractice> getMyList(@RequestParam Integer studentId) {
        return socialPracticeService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public SocialPractice add(@RequestBody SocialPractice socialPractice) {
        return socialPracticeService.add(socialPractice);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        socialPracticeService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<SocialPractice> getPendingList() {
        return socialPracticeService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public SocialPractice approve(@PathVariable Integer id,
                                  @RequestParam String status,
                                  @RequestParam String comment) {
        return socialPracticeService.approve(id, status, comment);
    }
}
