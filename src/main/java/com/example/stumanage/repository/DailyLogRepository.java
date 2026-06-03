package com.example.stumanage.repository;

import com.example.stumanage.model.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailyLogRepository extends JpaRepository<DailyLog, Integer> {
    List<DailyLog> findByStudentId(Integer studentId);
    List<DailyLog> findByStudentIdAndType(Integer studentId, String type);  // 按类型筛选
}
