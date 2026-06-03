package com.example.stumanage.service;

import com.example.stumanage.model.CourseSelection;
import com.example.stumanage.repository.CourseSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseSelectionService {

    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    //获取一个学生所选的所有课程
    public List<CourseSelection> getCourseSelectionByStudentId(Integer studentId) {
        return courseSelectionRepository.findByStudentId(studentId);
    }

    //获取课程选择情况
    public List<CourseSelection> getCourseSelectionByCourseId(Integer courseId) {
        return courseSelectionRepository.findByCourseId(courseId);
    }

    //获取所有课程选择情况
    public List<CourseSelection> getAllCourseSelection() {
        return courseSelectionRepository.findAll();
    }

    //添加选择的课程
    public CourseSelection addCourseSelection(CourseSelection courseSelection) {
        return courseSelectionRepository.save(courseSelection);
    }

    //更新选择的课程
    public CourseSelection updateCourseSelection(CourseSelection courseSelection) {
        return courseSelectionRepository.save(courseSelection);
    }

    //删除选择的课程
    public void deleteCourseSelection(Integer id) {
        courseSelectionRepository.deleteById(id);
    }
}