package com.example.stumanage.repository;

import com.example.stumanage.model.Honor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface HonorRepository extends JpaRepository<Honor, Integer>{
    List<Honor> findByStudentId(Integer studentId);
    List<Honor> findByStatus(String status);
}
