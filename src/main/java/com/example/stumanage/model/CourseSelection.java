package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "course_selection")
public class CourseSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联课程
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // 关联学生
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String status;        // 选课状态（已选/未选）
    private String selectTime;    // 选课时间
}
