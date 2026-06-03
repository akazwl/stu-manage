package com.example.stumanage.controller;

import com.example.stumanage.model.Assignment;
import com.example.stumanage.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@CrossOrigin(origins = "*")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    //=======学生端=======

    // 查看某门课的所有作业
    @GetMapping("/list/{courseId}")
    public List<Assignment> getAssignmentByCourse(@PathVariable Integer courseId) {
        return assignmentService.getAssignmentByCourseId(courseId);
    }

    //=======教师端=======

    // 查看所有作业
    @GetMapping("/all")
    public List<Assignment> getAllAssignment() {
        return assignmentService.getAllAssignment();
    }

    // 新增作业
    @PostMapping("/add")
    public Assignment addAssignment(@RequestBody Assignment assignment) {
        return assignmentService.addAssignment(assignment);
    }

    // 修改作业
    @PutMapping("/update")
    public Assignment updateAssignment(@RequestBody Assignment assignment) {
        return assignmentService.updateAssignment(assignment);
    }

    // 删除作业
    @DeleteMapping("/delete/{id}")
    public void deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
    }
}