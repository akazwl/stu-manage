package com.example.stumanage.controller;

import com.example.stumanage.model.SocialRelation;
import com.example.stumanage.service.SocialRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/social-relation")
@CrossOrigin(origins = "*")
public class SocialRelationController {
    @Autowired
    private SocialRelationService socialRelationService;

    //=======学生端=======

    // 查看自己的社会关系
    @GetMapping("/my-list")
    public List<SocialRelation> getMySocialRelation(@RequestParam Integer personId) {
        return socialRelationService.getSocialRelationByPersonId(personId);
    }

    // 新增社会关系
    @PostMapping("/add")
    public SocialRelation addSocialRelation(@RequestBody SocialRelation socialRelation) {
        return socialRelationService.addSocialRelation(socialRelation);
    }

    // 修改社会关系
    @PutMapping("/update")
    public SocialRelation updateSocialRelation(@RequestBody SocialRelation socialRelation) {
        return socialRelationService.updateSocialRelation(socialRelation);
    }

    // 删除社会关系
    @DeleteMapping("/delete/{id}")
    public void deleteSocialRelation(@PathVariable Integer id) {
        socialRelationService.deleteSocialRelation(id);
    }

    //=======教师端=======

    // 查看所有学生的社会关系
    @GetMapping("/all")
    public List<SocialRelation> getAllSocialRelation() {
        return socialRelationService.getAllSocialRelation();
    }

    // 查看某个学生的社会关系
    @GetMapping("/list/{personId}")
    public List<SocialRelation> getSocialRelationByStudent(@PathVariable Integer personId) {
        return socialRelationService.getSocialRelationByPersonId(personId);
    }

    // 教师端新增
    @PostMapping("/teacher/add")
    public SocialRelation teacherAddSocialRelation(@RequestBody SocialRelation socialRelation) {
        return socialRelationService.addSocialRelation(socialRelation);
    }

    // 教师端修改
    @PutMapping("/teacher/update")
    public SocialRelation teacherUpdateSocialRelation(@RequestBody SocialRelation socialRelation) {
        return socialRelationService.updateSocialRelation(socialRelation);
    }

    // 教师端删除
    @DeleteMapping("/teacher/delete/{id}")
    public void teacherDeleteSocialRelation(@PathVariable Integer id) {
        socialRelationService.deleteSocialRelation(id);
    }
}
