package com.example.stumanage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InnovationProjectControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/innovation-project/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddAndReturnPending() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "projectName": "测试创新项目",
                  "level": "省级",
                  "role": "负责人",
                  "startDate": "2024-03-01",
                  "endDate": "2025-03-01",
                  "projectStatus": "进行中",
                  "description": "测试描述"
                }
                """;
        mockMvc.perform(post("/api/innovation-project/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.projectStatus").value("进行中"));
    }

    @Test
    void shouldUpdateProjectStatus() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "projectName": "进度更新测试项目",
                  "level": "校级",
                  "role": "成员",
                  "startDate": "2024-01-01",
                  "endDate": "2024-12-01",
                  "projectStatus": "进行中",
                  "description": "测试进度更新"
                }
                """;
        String result = mockMvc.perform(post("/api/innovation-project/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(result);
        m.find();
        int id = Integer.parseInt(m.group(1));
        mockMvc.perform(put("/api/innovation-project/update-status/" + id)
                        .param("projectStatus", "已结题"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectStatus").value("已结题"));

        mockMvc.perform(delete("/api/innovation-project/delete/" + id));
    }

    @Test
    void shouldGetPendingList() throws Exception {
        mockMvc.perform(get("/api/innovation-project/pending-list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldApprovePendingRecord() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "projectName": "审批测试项目",
                  "level": "国家级",
                  "role": "负责人",
                  "startDate": "2024-01-01",
                  "endDate": "2025-06-01",
                  "projectStatus": "进行中",
                  "description": "测试审批流程"
                }
                """;
        String result = mockMvc.perform(post("/api/innovation-project/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(result);
        m.find();
        int id = Integer.parseInt(m.group(1));
        mockMvc.perform(put("/api/innovation-project/approve/" + id)
                        .param("status", "APPROVED")
                        .param("comment", "审批通过-集成测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc.perform(delete("/api/innovation-project/delete/" + id));
    }
}
