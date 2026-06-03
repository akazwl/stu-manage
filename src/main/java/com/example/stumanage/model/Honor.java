package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "honor")
public class Honor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private String name;
    private String level;
    private LocalDate awardDate;
    private String description;

    private String status = "PENDING";
    private String teacherComment;
}
