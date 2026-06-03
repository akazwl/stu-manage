package com.example.stumanage.controller;

import com.example.stumanage.model.InnovationProject;
import com.example.stumanage.service.InnovationProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/innovation-project")
@CrossOrigin(origins = "*")
public class InnovationProjectController {

    @Autowired
    private InnovationProjectService innovationProjectService;

    //=======学生端=======
    @GetMapping("/my-list")
    public List<InnovationProject> getMyList(@RequestParam Integer studentId) {
        return innovationProjectService.getByStudent(studentId);
    }

    @PostMapping("/add")
    public InnovationProject add(@RequestBody InnovationProject project) {
        return innovationProjectService.add(project);
    }

    @PutMapping("/update-status/{id}")
    public InnovationProject updateProjectStatus(@PathVariable Integer id,
                                                 @RequestParam String projectStatus) {
        return innovationProjectService.updateProjectStatus(id, projectStatus);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        innovationProjectService.delete(id);
    }

    //=======教师端=======
    @GetMapping("/pending-list")
    public List<InnovationProject> getPendingList() {
        return innovationProjectService.getAllPending();
    }

    @PutMapping("/approve/{id}")
    public InnovationProject approve(@PathVariable Integer id,
                                     @RequestParam String status,
                                     @RequestParam String comment) {
        return innovationProjectService.approve(id, status, comment);
    }
}
