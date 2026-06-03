package com.example.stumanage.service;

import com.example.stumanage.model.CampusAccess;
import com.example.stumanage.repository.CampusAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional

public class CampusAccessService {

    @Autowired
    private CampusAccessRepository accessRepository;
//学生端
    //获取某个学生出入申请
    public List<CampusAccess> getAccessesByStudent(Integer id){
        return accessRepository.findByStudentId(id);
    }

    //学生提交新的出入申请
    public CampusAccess addAccess(CampusAccess access){
        access.setStatus("PENDING");
        access.setApplyTime(LocalDateTime.now());
        return accessRepository.save(access);
    }

    //学生修改自己的申请（PENDING状态）
    public CampusAccess updateAccess(CampusAccess access, Integer id){
        if(access.getId() == null) return null;
        CampusAccess existing = accessRepository.findById(access.getId()).orElse(null);
        if(existing == null) return null;
        if(!existing.getStudent().getId().equals(id)||!"PENDING".equals(existing.getStatus())) return null;

        existing.setStartTime(access.getStartTime());
        existing.setEndTime(access.getEndTime());
        existing.setReason(access.getReason());
        return accessRepository.save(existing);
    }

    //学生删除自己的申请（PENDING状态）
    public boolean deleteAccess(Integer id, Integer studentId){
        CampusAccess existing = accessRepository.findById(id).orElse(null);

        if(existing == null) return false;
        if(!existing.getStudent().getId().equals(studentId)||!"PENDING".equals(existing.getStatus())) return false;

        accessRepository.deleteById(id);
        return true;
    }
//教师端
    //教师获取所有待审批的出入申请
    public List<CampusAccess> getPendingAccess(){
        return accessRepository.findByStatus("PENDING");
    }

    //教师审批出入申请（yes/no）
    public CampusAccess approveAccess(Integer id, String status, String comment){
        CampusAccess access = accessRepository.findById(id).orElse(null);
        if(access == null) return null;

        if("PENDING".equals(access.getStatus())){
            access.setStatus(status);
            access.setTeacherComment(comment);
            return accessRepository.save(access);
        }
        return null;
    }

}
