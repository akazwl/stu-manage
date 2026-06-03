package com.example.stumanage.controller;

import com.example.stumanage.model.CampusAccess;
import com.example.stumanage.service.CampusAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class CampusAccessController {

    @Autowired
    private CampusAccessService accessService;

    //=======学生端=======
    //获取自己的出入申请
    @GetMapping("/my-list")
    public List<CampusAccess> getMyAccesses(@RequestParam Integer studentId){
        return accessService.getAccessesByStudent(studentId);
    }

    //提交申请
    @PostMapping("/add")
    public CampusAccess addAccess(@RequestBody CampusAccess access){
        return accessService.addAccess(access);
    }

    //修改申请
    @PutMapping("/update")
    public CampusAccess updateAccess(@RequestBody CampusAccess access, @RequestParam Integer studentId){
        return accessService.updateAccess(access, studentId);
    }

    //删除申请
    @DeleteMapping("/delete/{id}")
    public boolean deleteAccess(@PathVariable Integer id, @RequestParam Integer studentId){
        return accessService.deleteAccess(id, studentId);
    }

    //=======教师端=======
    //获取所有待审批申请
    @GetMapping("pending-list")
    public List<CampusAccess> getPendingAccesses(){
        return accessService.getPendingAccess();
    }

    //审批申请
    @PutMapping("/approve/{id}")
    public CampusAccess approveAccess(@PathVariable Integer id, @RequestParam String status, @RequestParam String comment){
        return accessService.approveAccess(id, status, comment);
    }
}
