package com.example.stumanage.controller;

import com.example.stumanage.model.Honor;
import com.example.stumanage.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/honor")
@CrossOrigin(origins = "*")
public class HonorController {

    @Autowired
    private HonorService honorService;
    //=======学生端=======
    //获取荣誉列表
    @GetMapping("/my-list")
    public List<Honor> getMyHonors(@RequestParam Integer studentId){
        return honorService.getHonorsByStudent(studentId);
    }

    //添加荣誉
    @PostMapping("/add")
    public Honor addHonor(@RequestBody Honor honor){
        return honorService.addHonor(honor);
    }

    //删除荣誉（PENDING状态）
    @DeleteMapping("/delete/{id}")
    public void deleteHonor(@PathVariable Integer id){
        honorService.deleteHonor(id);
    }

    //=======教师端=======
    //获取所有待审批荣誉
    @GetMapping("/pending-list")
    public List<Honor> getPendingHonors(){
        return honorService.getAllPendingHonors();
    }

    //审批荣誉
    @PutMapping("/approve/{id}")
    public Honor approveHonor(@PathVariable Integer id, @RequestParam String status, @RequestParam String comment){
        return honorService.approveHonor(id, status, comment);
    }
}
