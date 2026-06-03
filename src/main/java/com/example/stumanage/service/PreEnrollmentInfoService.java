package com.example.stumanage.service;

import com.example.stumanage.model.PreEnrollmentInfo;
import com.example.stumanage.repository.PreEnrollmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PreEnrollmentInfoService {

    @Autowired
    private PreEnrollmentInfoRepository preEnrollmentInfoRepository;

    //查询入学前信息
    public PreEnrollmentInfo getPreEnrollmentInfoByPersonId(Integer id){
        return preEnrollmentInfoRepository.findByPersonId(id);
    }

    //查询所有学生的入学前信息
    public List<PreEnrollmentInfo> getAllPreEnrollmentInfo(){
        return preEnrollmentInfoRepository.findAll();
    }

    //新增入学前信息
    public PreEnrollmentInfo savePreEnrollmentInfo(PreEnrollmentInfo preEnrollmentInfo){
        return preEnrollmentInfoRepository.save(preEnrollmentInfo);
    }

    //修改入学前信息
    public PreEnrollmentInfo updatePreEnrollmentInfo(PreEnrollmentInfo info) {
        return preEnrollmentInfoRepository.save(info);
    }

    //删除信息
    public void deletePreEnrollmentInfo(Integer id){
        preEnrollmentInfoRepository.deleteById(id);
    }

}
