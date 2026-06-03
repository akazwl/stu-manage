package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联教师
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Person teacher;

    private String courseName;     // 课程名称
    private String courseCode;     // 课程编号
    private String credit;         // 学分
    private String hours;          // 课时
    private String classroom;      // 上课地点
    private String courseTime;     // 上课时间
    private String semester;       // 学期
    private String description;    // 课程简介
}
