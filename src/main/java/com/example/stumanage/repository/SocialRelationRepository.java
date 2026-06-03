package com.example.stumanage.repository;

import com.example.stumanage.model.SocialRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SocialRelationRepository extends JpaRepository<SocialRelation, Integer>{
    List<SocialRelation> findByPersonId(Integer personId);
}
