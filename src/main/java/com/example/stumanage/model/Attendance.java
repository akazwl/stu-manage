package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance")
public class Attendance {

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

    private String attendanceDate;  // 考勤日期
    private String status;          // 状态（出勤/缺勤/请假/迟到）
    private String remark;          // 备注
}
