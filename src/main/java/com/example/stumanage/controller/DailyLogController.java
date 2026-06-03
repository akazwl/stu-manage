package com.example.stumanage.controller;

import com.example.stumanage.model.DailyLog;
import com.example.stumanage.service.DailyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-log")
@CrossOrigin(origins = "*")
public class DailyLogController {

    @Autowired
    private DailyLogService dailyLogService;

    // 查看我的所有日志
    @GetMapping("/my-list")
    public List<DailyLog> getMyList(@RequestParam Integer studentId) {
        return dailyLogService.getByStudent(studentId);
    }

    // 按类型筛选日志（如只查消费类）
    @GetMapping("/my-list/type/{type}")
    public List<DailyLog> getByType(@RequestParam Integer studentId,
                                    @PathVariable String type) {
        return dailyLogService.getByStudentAndType(studentId, type);
    }

    // 新增日志
    @PostMapping("/add")
    public DailyLog add(@RequestBody DailyLog log) {
        return dailyLogService.add(log);
    }

    // 修改日志
    @PutMapping("/update")
    public DailyLog update(@RequestBody DailyLog log) {
        return dailyLogService.update(log);
    }

    // 删除日志
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        dailyLogService.delete(id);
    }
}
