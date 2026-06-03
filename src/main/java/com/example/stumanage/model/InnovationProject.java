package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "innovation_project")
public class InnovationProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String projectName;   // 项目名称
    private String level;         // 项目级别（国家级/省级/校级等）
    private String role;          // 参与角色（负责人/成员等）
    private String startDate;     // 开始时间
    private String endDate;       // 结束时间
    private String projectStatus; // 项目状态（进行中/已结题等）

    private String status = "PENDING";   // 审批状态：PENDING / APPROVED / REJECTED
    private String teacherComment;       // 教师审批意见
    private String description;   // 项目描述
}
