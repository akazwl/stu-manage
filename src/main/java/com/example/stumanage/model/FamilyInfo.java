package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "family_info")
public class FamilyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //关联到Person
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private String memberName;
    private String relation;
    private String phone;
    private String workplace;
    private String occupation;
}
