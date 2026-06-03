package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "campus_access")
public class CampusAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;

    private String status = "PENDING";
    private String teacherComment;
    private LocalDateTime applyTime;
}
