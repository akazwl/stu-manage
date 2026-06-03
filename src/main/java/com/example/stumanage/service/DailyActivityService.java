package com.example.stumanage.service;

import com.example.stumanage.model.DailyActivity;
import com.example.stumanage.repository.DailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DailyActivityService {

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    // 查看我的日常活动列表
    public List<DailyActivity> getByStudent(Integer studentId) {
        return dailyActivityRepository.findByStudentId(studentId);
    }

    // 新增日常活动
    public DailyActivity add(DailyActivity activity) {
        return dailyActivityRepository.save(activity);
    }

    // 删除日常活动
    public void delete(Integer id) {
        dailyActivityRepository.deleteById(id);
    }
}
