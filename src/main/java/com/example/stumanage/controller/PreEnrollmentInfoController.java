package com.example.stumanage.controller;

import com.example.stumanage.model.PreEnrollmentInfo;
import com.example.stumanage.service.PreEnrollmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pre-enrollment")
@CrossOrigin(origins = "*")
public class PreEnrollmentInfoController {

    @Autowired
    private PreEnrollmentInfoService preEnrollmentInfoService;

    //=======学生端=======

    // 查看自己的入学前信息
    @GetMapping("/my-info")
    public PreEnrollmentInfo getMyPreEnrollmentInfo(@RequestParam Integer personId) {
        return preEnrollmentInfoService.getPreEnrollmentInfoByPersonId(personId);
    }

    // 新增入学前信息
    @PostMapping("/add")
    public PreEnrollmentInfo savePreEnrollmentInfo(@RequestBody PreEnrollmentInfo preEnrollmentInfo) {
        return preEnrollmentInfoService.savePreEnrollmentInfo(preEnrollmentInfo);
    }

    // 修改入学前信息
    @PutMapping("/update")
    public PreEnrollmentInfo updatePreEnrollmentInfo(@RequestBody PreEnrollmentInfo preEnrollmentInfo) {
        return preEnrollmentInfoService.updatePreEnrollmentInfo(preEnrollmentInfo);
    }

    // 删除入学前信息
    @DeleteMapping("/delete/{id}")
    public void deletePreEnrollmentInfo(@PathVariable Integer id) {
        preEnrollmentInfoService.deletePreEnrollmentInfo(id);
    }

    //=======教师端=======

    // 查看所有学生的入学前信息
    @GetMapping("/all")
    public List<PreEnrollmentInfo> getAllPreEnrollmentInfo() {
        return preEnrollmentInfoService.getAllPreEnrollmentInfo();
    }

    // 查看某个学生的入学前信息
    @GetMapping("/list/{personId}")
    public PreEnrollmentInfo getPreEnrollmentInfoByStudent(@PathVariable Integer personId) {
        return preEnrollmentInfoService.getPreEnrollmentInfoByPersonId(personId);
    }

    // 教师端新增
    @PostMapping("/teacher/add")
    public PreEnrollmentInfo teacherSavePreEnrollmentInfo(@RequestBody PreEnrollmentInfo preEnrollmentInfo) {
        return preEnrollmentInfoService.savePreEnrollmentInfo(preEnrollmentInfo);
    }

    // 教师端修改
    @PutMapping("/teacher/update")
    public PreEnrollmentInfo teacherUpdatePreEnrollmentInfo(@RequestBody PreEnrollmentInfo preEnrollmentInfo) {
        return preEnrollmentInfoService.updatePreEnrollmentInfo(preEnrollmentInfo);
    }

    // 教师端删除
    @DeleteMapping("/teacher/delete/{id}")
    public void teacherDeletePreEnrollmentInfo(@PathVariable Integer id) {
        preEnrollmentInfoService.deletePreEnrollmentInfo(id);
    }
}
