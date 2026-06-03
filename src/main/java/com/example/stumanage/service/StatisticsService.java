package com.example.stumanage.service;

import com.example.stumanage.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsService {

    @Autowired private CompetitionRepository competitionRepo;
    @Autowired private ScienceAchievementRepository scienceAchievementRepo;
    @Autowired private InternshipRepository internshipRepo;
    @Autowired private InnovationProjectRepository innovationProjectRepo;
    @Autowired private TrainingLectureRepository trainingLectureRepo;
    @Autowired private SocialPracticeRepository socialPracticeRepo;
    @Autowired private DailyActivityRepository dailyActivityRepo;
    @Autowired private DailyLogRepository dailyLogRepo;
    @Autowired private UserRepository userRepo;

    /**
     * 学生个人仪表盘 — 统计自己各模块的数据量
     */
    public Map<String, Object> getMyDashboard(Integer studentId) {
        Map<String, Object> dashboard = new LinkedHashMap<>();

        // 各模块总数
        dashboard.put("竞赛", competitionRepo.findByStudentId(studentId).size());
        dashboard.put("科技成果", scienceAchievementRepo.findByStudentId(studentId).size());
        dashboard.put("实习", internshipRepo.findByStudentId(studentId).size());
        dashboard.put("创新项目", innovationProjectRepo.findByStudentId(studentId).size());
        dashboard.put("培训讲座", trainingLectureRepo.findByStudentId(studentId).size());
        dashboard.put("社会实践", socialPracticeRepo.findByStudentId(studentId).size());
        dashboard.put("日常活动", dailyActivityRepo.findByStudentId(studentId).size());
        dashboard.put("日志", dailyLogRepo.findByStudentId(studentId).size());

        // 审批状态统计（只统计有审批流的6个模块）
        long approved = countApproved(studentId);
        long pending = countPending(studentId);
        long rejected = countRejected(studentId);
        dashboard.put("审批通过", approved);
        dashboard.put("待审批", pending);
        dashboard.put("已驳回", rejected);

        return dashboard;
    }

    /**
     * 教师/管理员总览 — 看全系统概况
     */
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new LinkedHashMap<>();

        // 全系统各模块数据量
        overview.put("竞赛总数", competitionRepo.count());
        overview.put("科技成果总数", scienceAchievementRepo.count());
        overview.put("实习总数", internshipRepo.count());
        overview.put("创新项目总数", innovationProjectRepo.count());
        overview.put("培训讲座总数", trainingLectureRepo.count());
        overview.put("社会实践总数", socialPracticeRepo.count());
        overview.put("日常活动总数", dailyActivityRepo.count());
        overview.put("日志总数", dailyLogRepo.count());

        // 待审批数量（教师常用）
        overview.put("竞赛待审批", competitionRepo.findByStatus("PENDING").size());
        overview.put("科技成果待审批", scienceAchievementRepo.findByStatus("PENDING").size());
        overview.put("实习待审批", internshipRepo.findByStatus("PENDING").size());
        overview.put("创新项目待审批", innovationProjectRepo.findByStatus("PENDING").size());
        overview.put("培训讲座待审批", trainingLectureRepo.findByStatus("PENDING").size());
        overview.put("社会实践待审批", socialPracticeRepo.findByStatus("PENDING").size());

        // 用户分布
        overview.put("管理员人数", userRepo.findByRole("ADMIN").size());
        overview.put("教师人数", userRepo.findByRole("TEACHER").size());
        overview.put("学生人数", userRepo.findByRole("STUDENT").size());

        return overview;
    }

    // 统计某个学生审批通过的数量
    private long countApproved(Integer studentId) {
        return countByStatus(studentId, "APPROVED");
    }

    // 统计某个学生待审批的数量
    private long countPending(Integer studentId) {
        return countByStatus(studentId, "PENDING");
    }

    // 统计某个学生被驳回的数量
    private long countRejected(Integer studentId) {
        return countByStatus(studentId, "REJECTED");
    }

    // 遍历6个审批模块，按状态计数
    private long countByStatus(Integer studentId, String status) {
        long count = 0;
        count += competitionRepo.findByStudentId(studentId).stream()
                .filter(c -> status.equals(c.getStatus())).count();
        count += scienceAchievementRepo.findByStudentId(studentId).stream()
                .filter(s -> status.equals(s.getStatus())).count();
        count += internshipRepo.findByStudentId(studentId).stream()
                .filter(i -> status.equals(i.getStatus())).count();
        count += innovationProjectRepo.findByStudentId(studentId).stream()
                .filter(p -> status.equals(p.getStatus())).count();
        count += trainingLectureRepo.findByStudentId(studentId).stream()
                .filter(t -> status.equals(t.getStatus())).count();
        count += socialPracticeRepo.findByStudentId(studentId).stream()
                .filter(sp -> status.equals(sp.getStatus())).count();
        return count;
    }
}
