package com.example.stumanage.service;

import com.example.stumanage.model.Attendance;
import com.example.stumanage.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceByStudentId(Integer studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByCourseId(Integer courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }

    public List<Attendance> getAttendanceByCourseIdAndStudentId(Integer courseId, Integer studentId) {
        return attendanceRepository.findByCourseIdAndStudentId(courseId, studentId);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance addAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance updateAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Integer id) {
        attendanceRepository.deleteById(id);
    }
}