package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "social_practice")
public class SocialPractice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String practiceName;   // 实践名称
    private String location;       // 地点
    private String startDate;      // 开始时间
    private String endDate;        // 结束时间
    private String role;           // 担任角色
    private String description;    // 描述

    private String status = "PENDING";
    private String teacherComment;
}