package com.example.stumanage.repository;

import com.example.stumanage.model.PreEnrollmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreEnrollmentInfoRepository extends JpaRepository<PreEnrollmentInfo, Integer>{
    PreEnrollmentInfo findByPersonId(Integer personId);
}
