package com.example.stumanage.controller;

import com.example.stumanage.model.FamilyInfo;
import com.example.stumanage.service.FamilyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/family")
@CrossOrigin(origins = "*")
public class FamilyInfoController {

    @Autowired
    private FamilyInfoService familyInfoService;

    //=======学生端=======

    // 查看自己的家庭信息
    @GetMapping("/my-list")
    public List<FamilyInfo> getMyFamilyInfo(@RequestParam Integer personId) {
        return familyInfoService.getFamilyInfoByPersonId(personId);
    }

    // 新增家庭成员
    @PostMapping("/add")
    public FamilyInfo addFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        return familyInfoService.addFamilyInfo(familyInfo);
    }

    // 修改家庭成员
    @PutMapping("/update")
    public FamilyInfo updateFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        return familyInfoService.updateFamilyInfo(familyInfo);
    }

    // 删除家庭成员
    @DeleteMapping("/delete/{id}")
    public void deleteFamilyInfo(@PathVariable Integer id) {
        familyInfoService.deleteFamilyInfo(id);
    }

    //=======教师端=======

    // 查看所有学生的家庭信息
    @GetMapping("/all")
    public List<FamilyInfo> getAllFamilyInfo() {
        return familyInfoService.getAllFamilyInfo();
    }

    // 查看某个学生的家庭信息
    @GetMapping("/list/{personId}")
    public List<FamilyInfo> getFamilyInfoByStudent(@PathVariable Integer personId) {
        return familyInfoService.getFamilyInfoByPersonId(personId);
    }

    // 教师端新增
    @PostMapping("/teacher/add")
    public FamilyInfo teacherAddFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        return familyInfoService.addFamilyInfo(familyInfo);
    }

    // 教师端修改
    @PutMapping("/teacher/update")
    public FamilyInfo teacherUpdateFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        return familyInfoService.updateFamilyInfo(familyInfo);
    }

    // 教师端删除
    @DeleteMapping("/teacher/delete/{id}")
    public void teacherDeleteFamilyInfo(@PathVariable Integer id) {
        familyInfoService.deleteFamilyInfo(id);
    }
}
