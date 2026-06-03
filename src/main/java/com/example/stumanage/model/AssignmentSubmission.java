package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "assignment_submission")
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联作业
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    // 关联学生
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String content;        // 提交内容
    private String submitTime;     // 提交时间
    private String score;          // 得分（老师打分）
    private String feedback;       // 老师反馈
    private String status;         // 状态（未提交/已提交/已批改）
}
