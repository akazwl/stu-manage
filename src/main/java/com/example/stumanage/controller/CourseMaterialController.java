package com.example.stumanage.controller;

import com.example.stumanage.model.CourseMaterial;
import com.example.stumanage.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course-material")
@CrossOrigin(origins = "*")
public class CourseMaterialController {

    @Autowired
    private CourseMaterialService courseMaterialService;

    //=======学生端=======

    // 查看某门课的所有资料
    @GetMapping("/list/{courseId}")
    public List<CourseMaterial> getCourseMaterialByCourse(@PathVariable Integer courseId) {
        return courseMaterialService.getCourseMaterialByCourseId(courseId);
    }

    //=======教师端=======

    // 查看所有课程资料
    @GetMapping("/all")
    public List<CourseMaterial> getAllCourseMaterial() {
        return courseMaterialService.getAllCourseMaterial();
    }

    // 新增课程资料
    @PostMapping("/add")
    public CourseMaterial addCourseMaterial(@RequestBody CourseMaterial courseMaterial) {
        return courseMaterialService.addCourseMaterial(courseMaterial);
    }

    // 修改课程资料
    @PutMapping("/update")
    public CourseMaterial updateCourseMaterial(@RequestBody CourseMaterial courseMaterial) {
        return courseMaterialService.updateCourseMaterial(courseMaterial);
    }

    // 删除课程资料
    @DeleteMapping("/delete/{id}")
    public void deleteCourseMaterial(@PathVariable Integer id) {
        courseMaterialService.deleteCourseMaterial(id);
    }
}