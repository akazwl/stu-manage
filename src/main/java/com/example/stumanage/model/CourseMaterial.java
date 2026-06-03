package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "course_material")
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联课程
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;        // 资料标题
    private String type;         // 类型（教材/课件/参考资料）
    private String url;          // 资料链接
    private String description;  // 资料描述
}
