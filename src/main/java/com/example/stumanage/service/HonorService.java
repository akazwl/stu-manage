package com.example.stumanage.service;

import com.example.stumanage.model.Honor;
import com.example.stumanage.repository.HonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HonorService {

    @Autowired
    private HonorRepository honorRepository;

    public List<Honor> getHonorsByStudent(Integer studentId) {
        return honorRepository.findByStudentId(studentId);
    }

    public Honor addHonor(Honor honor) {
        honor.setStatus("PENDING");
        return honorRepository.save(honor);
    }

    public void deleteHonor(Integer id) {
        honorRepository.deleteById(id);
    }

    public List<Honor> getAllPendingHonors() {
        return honorRepository.findByStatus("PENDING");
    }

    public Honor approveHonor(Integer id, String status, String comment) {
        Honor honor = honorRepository.findById(id).orElse(null);
        if (honor != null) {
            if ("PENDING".equals(honor.getStatus())) {
                honor.setStatus(status);
                honor.setTeacherComment(comment);}
            return honorRepository.save(honor);
        }
        return null;
    }
}
