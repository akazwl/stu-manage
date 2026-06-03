package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String competitionName;  // 竞赛名称
    private String level;            // 竞赛级别（国家级/省级/校级等）
    private String award;            // 获奖情况（一等奖/二等奖等）
    private String awardDate;        // 获奖时间
    private String organizer;        // 主办单位
    private String description;      // 描述

    private String status = "PENDING";// PENDING, APPROVED, REJECTED
    private String teacherComment;   // 教师审批意见
}
