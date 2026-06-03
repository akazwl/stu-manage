package com.example.stumanage.controller;

import com.example.stumanage.model.AssignmentSubmission;
import com.example.stumanage.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assignment-submission")
@CrossOrigin(origins = "*")
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    //=======学生端=======

    // 查看自己的作业提交记录
    @GetMapping("/my-list")
    public List<AssignmentSubmission> getMyAssignmentSubmission(@RequestParam Integer studentId) {
        return assignmentSubmissionService.getAssignmentSubmissionByStudentId(studentId);
    }

    // 提交作业
    @PostMapping("/add")
    public AssignmentSubmission addAssignmentSubmission(@RequestBody AssignmentSubmission assignmentSubmission) {
        return assignmentSubmissionService.addAssignmentSubmission(assignmentSubmission);
    }

    // 修改提交的作业
    @PutMapping("/update")
    public AssignmentSubmission updateAssignmentSubmission(@RequestBody AssignmentSubmission assignmentSubmission) {
        return assignmentSubmissionService.updateAssignmentSubmission(assignmentSubmission);
    }

    //=======教师端=======

    // 查看某个作业的所有提交记录
    @GetMapping("/list/{assignmentId}")
    public List<AssignmentSubmission> getAssignmentSubmissionByAssignment(@PathVariable Integer assignmentId) {
        return assignmentSubmissionService.getAssignmentSubmissionByAssignmentId(assignmentId);
    }

    // 打分（修改提交记录里的score和feedback）
    @PutMapping("/grade")
    public AssignmentSubmission gradeAssignmentSubmission(@RequestBody AssignmentSubmission assignmentSubmission) {
        return assignmentSubmissionService.updateAssignmentSubmission(assignmentSubmission);
    }

    // 删除提交记录
    @DeleteMapping("/delete/{id}")
    public void deleteAssignmentSubmission(@PathVariable Integer id) {
        assignmentSubmissionService.deleteAssignmentSubmission(id);
    }
}