package com.example.stumanage.service;

import com.example.stumanage.model.FamilyInfo;
import com.example.stumanage.repository.FamilyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FamilyInfoService {

    @Autowired
    private FamilyInfoRepository familyInfoRepository;

    //查询某人家庭成员
    public List<FamilyInfo> getFamilyInfoByPersonId(Integer personId){
        return familyInfoRepository.findByPersonId(personId);
    }

    //获取所有家庭成员
    public List<FamilyInfo> getAllFamilyInfo(){
        return familyInfoRepository.findAll();
    }

    //新增家庭成员
    public FamilyInfo addFamilyInfo(FamilyInfo familyInfo){
        return familyInfoRepository.save(familyInfo);
    }

    //修改家庭成员信息
    public FamilyInfo updateFamilyInfo(FamilyInfo familyInfo){
        return familyInfoRepository.save(familyInfo);
    }

    //删除家庭成员
    public void deleteFamilyInfo(Integer id){
        familyInfoRepository.deleteById(id);
    }
}
