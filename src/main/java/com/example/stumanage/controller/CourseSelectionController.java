package com.example.stumanage.controller;

import com.example.stumanage.model.CourseSelection;
import com.example.stumanage.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course-selection")
@CrossOrigin(origins = "*")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    //=======学生端=======

    // 查看自己的选课记录
    @GetMapping("/my-list")
    public List<CourseSelection> getMyCourseSelection(@RequestParam Integer studentId) {
        return courseSelectionService.getCourseSelectionByStudentId(studentId);
    }

    // 选课
    @PostMapping("/add")
    public CourseSelection addCourseSelection(@RequestBody CourseSelection courseSelection) {
        return courseSelectionService.addCourseSelection(courseSelection);
    }

    // 退课
    @DeleteMapping("/delete/{id}")
    public void deleteCourseSelection(@PathVariable Integer id) {
        courseSelectionService.deleteCourseSelection(id);
    }

    //=======教师端=======

    // 查看所有选课记录
    @GetMapping("/all")
    public List<CourseSelection> getAllCourseSelection() {
        return courseSelectionService.getAllCourseSelection();
    }

    // 查看某门课的选课名单
    @GetMapping("/list/{courseId}")
    public List<CourseSelection> getCourseSelectionByCourse(@PathVariable Integer courseId) {
        return courseSelectionService.getCourseSelectionByCourseId(courseId);
    }
}