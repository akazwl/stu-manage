package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "daily_log")
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String title;      // 日志标题（如"今日消费记录"）
    private String type;       // 类型（消费/学习/生活/其他）
    private String logDate;    // 日期
    private String content;    // 日志内容
    private String mood;       // 心情（开心/一般/疲惫等）
    private String tags;       // 标签（用逗号分隔，如"学习,Java,图书馆"）
}
