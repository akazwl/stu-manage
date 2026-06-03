package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联课程
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;        // 作业标题
    private String description;  // 作业描述
    private String deadline;     // 截止时间
    private String totalScore;   // 满分
}
