package com.example.stumanage.repository;

import com.example.stumanage.model.CampusAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CampusAccessRepository extends JpaRepository<CampusAccess, Integer>{
    List<CampusAccess> findByStudentId(Integer studentId);
    List<CampusAccess> findByStatus(String status);
}
