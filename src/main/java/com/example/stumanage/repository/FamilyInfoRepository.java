package com.example.stumanage.repository;

import com.example.stumanage.model.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamilyInfoRepository extends JpaRepository<FamilyInfo, Integer>{
    List<FamilyInfo> findByPersonId(Integer personId);
}
