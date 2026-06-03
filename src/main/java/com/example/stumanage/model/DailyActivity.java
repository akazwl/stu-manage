package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "daily_activity")
public class DailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String activityName;   // 活动名称（如"篮球友谊赛"）
    private String type;           // 类型（体育/文艺/聚会/其他）
    private String activityDate;   // 活动时间
    private String location;       // 地点
    private String role;           // 角色（参与者/组织者）
    private String duration;       // 时长（小时）
    private String description;    // 活动描述
}
