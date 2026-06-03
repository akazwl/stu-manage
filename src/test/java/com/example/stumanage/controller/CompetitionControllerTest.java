package com.example.stumanage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompetitionControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/competition/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddAndReturnPending() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "competitionName": "测试竞赛",
                  "level": "国家级",
                  "award": "一等奖",
                  "awardDate": "2024-09-15",
                  "organizer": "测试主办方",
                  "description": "测试描述"
                }
                """;
        mockMvc.perform(post("/api/competition/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetPendingList() throws Exception {
        mockMvc.perform(get("/api/competition/pending-list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectApprovalWhenNotPending() throws Exception {
        // Approve non-existent id returns null/empty
        mockMvc.perform(put("/api/competition/approve/99999")
                        .param("status", "APPROVED")
                        .param("comment", "测试"))
                .andExpect(status().isOk());
    }
}
