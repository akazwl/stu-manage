package com.example.stumanage.controller;

import com.example.stumanage.model.Course;
import com.example.stumanage.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    //=======学生端=======

    // 查看所有课程
    @GetMapping("/all")
    public List<Course> getAllCourse() {
        return courseService.getAllCourse();
    }

    //=======教师端=======

    // 查看自己创建的课程
    @GetMapping("/my-list")
    public List<Course> getMyCourse(@RequestParam Integer teacherId) {
        return courseService.getCourseByTeacherId(teacherId);
    }

    // 新增课程
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course);
    }

    // 修改课程
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    // 删除课程
    @DeleteMapping("/delete/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
    }
}