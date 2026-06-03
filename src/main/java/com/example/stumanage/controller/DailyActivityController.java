package com.example.stumanage.controller;

import com.example.stumanage.model.DailyActivity;
import com.example.stumanage.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-activity")
@CrossOrigin(origins = "*")
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;

    // 查看我的日常活动列表
    @GetMapping("/my-list")
    public List<DailyActivity> getMyList(@RequestParam Integer studentId) {
        return dailyActivityService.getByStudent(studentId);
    }

    // 新增日常活动
    @PostMapping("/add")
    public DailyActivity add(@RequestBody DailyActivity activity) {
        return dailyActivityService.add(activity);
    }

    // 删除日常活动
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        dailyActivityService.delete(id);
    }
}
