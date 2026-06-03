package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "training_lecture")
public class TrainingLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String trainingName;   // 培训/讲座名称
    private String organizer;      // 主办方
    private String lectureDate;    // 时间
    private String location;       // 地点
    private String description;    // 描述

    private String status = "PENDING";
    private String teacherComment;
}