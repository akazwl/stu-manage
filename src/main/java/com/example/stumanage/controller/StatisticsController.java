package com.example.stumanage.controller;

import com.example.stumanage.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // 学生查看自己的数据仪表盘
    @GetMapping("/my-dashboard")
    public Map<String, Object> getMyDashboard(@RequestParam Integer studentId) {
        return statisticsService.getMyDashboard(studentId);
    }

    // 教师/管理员查看系统总览
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        return statisticsService.getOverview();
    }
}
