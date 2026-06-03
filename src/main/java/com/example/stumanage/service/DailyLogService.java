package com.example.stumanage.service;

import com.example.stumanage.model.DailyLog;
import com.example.stumanage.repository.DailyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DailyLogService {

    @Autowired
    private DailyLogRepository dailyLogRepository;

    // 查看我的所有日志
    public List<DailyLog> getByStudent(Integer studentId) {
        return dailyLogRepository.findByStudentId(studentId);
    }

    // 按类型筛选日志（消费/学习/生活/其他）
    public List<DailyLog> getByStudentAndType(Integer studentId, String type) {
        return dailyLogRepository.findByStudentIdAndType(studentId, type);
    }

    // 新增日志
    public DailyLog add(DailyLog log) {
        return dailyLogRepository.save(log);
    }

    // 修改日志
    public DailyLog update(DailyLog log) {
        return dailyLogRepository.save(log);
    }

    // 删除日志
    public void delete(Integer id) {
        dailyLogRepository.deleteById(id);
    }
}
