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
class TrainingLectureControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/training-lecture/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddAndReturnPending() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "trainingName": "测试培训",
                  "organizer": "测试主办方",
                  "lectureDate": "2024-10-10",
                  "location": "测试地点",
                  "description": "测试描述"
                }
                """;
        mockMvc.perform(post("/api/training-lecture/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetPendingList() throws Exception {
        mockMvc.perform(get("/api/training-lecture/pending-list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldApprovePendingRecord() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "trainingName": "审批测试培训",
                  "organizer": "审批测试主办方",
                  "lectureDate": "2024-11-01",
                  "location": "测试教室",
                  "description": "测试审批流程"
                }
                """;
        String result = mockMvc.perform(post("/api/training-lecture/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(result);
        m.find();
        int id = Integer.parseInt(m.group(1));
        mockMvc.perform(put("/api/training-lecture/approve/" + id)
                        .param("status", "APPROVED")
                        .param("comment", "审批通过-集成测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc.perform(delete("/api/training-lecture/delete/" + id));
    }
}
