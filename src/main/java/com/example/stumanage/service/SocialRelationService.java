package com.example.stumanage.service;

import com.example.stumanage.model.SocialRelation;
import com.example.stumanage.repository.SocialRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SocialRelationService {

    @Autowired
    private SocialRelationRepository socialRelationRepository;

    public List<SocialRelation> getSocialRelationByPersonId(Integer personId) {
        return socialRelationRepository.findByPersonId(personId);
    }

    public List<SocialRelation> getAllSocialRelation() {
        return socialRelationRepository.findAll();
    }

    public SocialRelation addSocialRelation(SocialRelation socialRelation) {
        return socialRelationRepository.save(socialRelation);
    }

    public SocialRelation updateSocialRelation(SocialRelation socialRelation) {
        return socialRelationRepository.save(socialRelation);
    }

    public void deleteSocialRelation(Integer id) {
        socialRelationRepository.deleteById(id);
    }
}
