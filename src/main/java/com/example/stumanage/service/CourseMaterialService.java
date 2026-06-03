package com.example.stumanage.service;

import com.example.stumanage.model.CourseMaterial;
import com.example.stumanage.repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseMaterialService {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    //查看所有课程资料
    public List<CourseMaterial> getAllCourseMaterial(){
        return courseMaterialRepository.findAll();
    }

    //查看特定课程的资料
    public List<CourseMaterial> getCourseMaterialByCourseId(Integer id){
        return courseMaterialRepository.findByCourseId(id);
    }

    //添加课程资料
    public CourseMaterial addCourseMaterial(CourseMaterial courseMaterial) {
        return courseMaterialRepository.save(courseMaterial);
    }

    //更新课程资料
    public CourseMaterial updateCourseMaterial(CourseMaterial courseMaterial) {
        return courseMaterialRepository.save(courseMaterial);
    }

    //删除课程资料
    public void deleteCourseMaterial(Integer id) {
        courseMaterialRepository.deleteById(id);
    }
}
