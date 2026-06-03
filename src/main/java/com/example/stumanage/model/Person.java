package com.example.stumanage.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String birthday;
    private String emergencyContact;
    private String emergencyPhone;
}
