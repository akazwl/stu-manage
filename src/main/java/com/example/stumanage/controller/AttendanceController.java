package com.example.stumanage.controller;

import com.example.stumanage.model.Attendance;
import com.example.stumanage.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    //=======学生端=======

    // 查看自己的考勤记录
    @GetMapping("/my-list")
    public List<Attendance> getMyAttendance(@RequestParam Integer studentId) {
        return attendanceService.getAttendanceByStudentId(studentId);
    }

    // 查看自己某门课的考勤记录
    @GetMapping("/my-course-list")
    public List<Attendance> getMyAttendanceByCourse(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        return attendanceService.getAttendanceByCourseIdAndStudentId(courseId, studentId);
    }

    //=======教师端=======

    // 查看所有考勤记录
    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    // 查看某门课的所有考勤记录
    @GetMapping("/list/{courseId}")
    public List<Attendance> getAttendanceByCourse(@PathVariable Integer courseId) {
        return attendanceService.getAttendanceByCourseId(courseId);
    }

    // 新增考勤记录
    @PostMapping("/add")
    public Attendance addAttendance(@RequestBody Attendance attendance) {
        return attendanceService.addAttendance(attendance);
    }

    // 修改考勤记录
    @PutMapping("/update")
    public Attendance updateAttendance(@RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(attendance);
    }

    // 删除考勤记录
    @DeleteMapping("/delete/{id}")
    public void deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
    }
}