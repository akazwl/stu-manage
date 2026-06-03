package com.example.stumanage.service;

import com.example.stumanage.model.InnovationProject;
import com.example.stumanage.repository.InnovationProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InnovationProjectService {

    @Autowired
    private InnovationProjectRepository innovationProjectRepository;

    //=======学生端=======
    public List<InnovationProject> getByStudent(Integer studentId) {
        return innovationProjectRepository.findByStudentId(studentId);
    }

    public InnovationProject add(InnovationProject project) {
        project.setStatus("PENDING");
        return innovationProjectRepository.save(project);
    }

    public InnovationProject updateProjectStatus(Integer id, String projectStatus) {
        InnovationProject project = innovationProjectRepository.findById(id).orElse(null);
        if (project != null) {
            project.setProjectStatus(projectStatus);
            return innovationProjectRepository.save(project);
        }
        return null;
    }

    public void delete(Integer id) {
        innovationProjectRepository.deleteById(id);
    }

    //=======教师端=======
    public List<InnovationProject> getAllPending() {
        return innovationProjectRepository.findByStatus("PENDING");
    }

    public InnovationProject approve(Integer id, String status, String comment) {
        InnovationProject project = innovationProjectRepository.findById(id).orElse(null);
        if (project != null && "PENDING".equals(project.getStatus())) {
            project.setStatus(status);
            project.setTeacherComment(comment);
            return innovationProjectRepository.save(project);
        }
        return null;
    }
}
