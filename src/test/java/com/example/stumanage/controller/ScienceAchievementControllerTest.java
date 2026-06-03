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
class ScienceAchievementControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/science-achievement/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddAndReturnPending() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "achievementName": "测试科技成果",
                  "type": "论文",
                  "publishDate": "2024-08-20",
                  "publisher": "测试期刊",
                  "description": "测试描述"
                }
                """;
        mockMvc.perform(post("/api/science-achievement/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetPendingList() throws Exception {
        mockMvc.perform(get("/api/science-achievement/pending-list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldApprovePendingRecord() throws Exception {
        // First add a record
        String json = """
                {
                  "student": {"id": 1},
                  "achievementName": "审批测试成果",
                  "type": "专利",
                  "publishDate": "2024-06-01",
                  "publisher": "国家知识产权局",
                  "description": "测试审批流程"
                }
                """;
        String result = mockMvc.perform(post("/api/science-achievement/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        // Extract id and approve
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(result);
        m.find();
        int id = Integer.parseInt(m.group(1));
        mockMvc.perform(put("/api/science-achievement/approve/" + id)
                        .param("status", "APPROVED")
                        .param("comment", "审批通过-集成测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        // Cleanup
        mockMvc.perform(delete("/api/science-achievement/delete/" + id));
    }
}
