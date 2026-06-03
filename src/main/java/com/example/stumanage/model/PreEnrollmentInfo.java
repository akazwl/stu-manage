package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pre_enrollment_info")
public class PreEnrollmentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;//关联到Person

    private String highSchool;
    private String highSchoolProvince;
    private String highSchoolCity;
    private String graduationYear;
    private String gaokaoScore;
    private String major;//录取专业
    private String admissionType;//录取方式（高考/保送/综招/强基/单招等）
}
