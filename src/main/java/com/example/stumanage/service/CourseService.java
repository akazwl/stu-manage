package com.example.stumanage.service;

import com.example.stumanage.model.Course;
import com.example.stumanage.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    //获取所有课程
    public List<Course> getAllCourse(){
        return courseRepository.findAll();
    }

    //获取老师的课程
    public List<Course> getCourseByTeacherId(Integer teacherId){
        return courseRepository.findByTeacherId(teacherId);
    }

    //添加课程
    public Course addCourse(Course course){
        return courseRepository.save(course);
    }

    //更新课程信息
    public Course updateCourse(Course course){
        return courseRepository.save(course);
    }

    //删除课程
    public void deleteCourse(Integer id){
        courseRepository.deleteById(id);
    }
}
