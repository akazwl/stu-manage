package com.example.stumanage.controller;

import com.example.stumanage.model.Internship;
import com.example.stumanage.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internship")
@CrossOrigin(origins = "*")
public class InternshipController {

    @Autowired
    private InternshipService internshipService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<Internship> getMyList(@RequestParam Integer studentId) {
        return internshipService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public Internship add(@RequestBody Internship internship) {
        return internshipService.add(internship);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        internshipService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<Internship> getPendingList() {
        return internshipService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public Internship approve(@PathVariable Integer id,
                              @RequestParam String status,
                              @RequestParam String comment) {
        return internshipService.approve(id, status, comment);
    }
}
