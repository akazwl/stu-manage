package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "science_achievement")
public class ScienceAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String achievementName;  // 成果名称
    private String type;             // 类型（论文/专利/软件著作权等）
    private String publishDate;      // 发表/获批时间
    private String publisher;        // 发表机构/期刊
    private String description;      // 描述

    private String status = "PENDING";   // 审批状态：PENDING / APPROVED / REJECTED
    private String teacherComment;       // 教师审批意见
}
