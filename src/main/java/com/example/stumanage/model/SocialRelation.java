package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "social_relation")
public class SocialRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private String name;//关系人姓名
    private String relation;//关系
    private String phone;
    private String workplace;//工作或学习单位
    private String position;//职务或职位
    private String remark;//备注
}
