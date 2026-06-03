package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "internship")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String companyName;   // 实习单位
    private String position;      // 实习岗位
    private String startDate;     // 开始时间
    private String endDate;       // 结束时间
    private String location;      // 实习地点
    private String description;   // 实习描述

    private String status = "PENDING";   // 审批状态：PENDING / APPROVED / REJECTED
    private String teacherComment;       // 教师审批意见
}
