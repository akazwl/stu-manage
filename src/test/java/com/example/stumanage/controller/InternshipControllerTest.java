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
class InternshipControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/internship/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddAndReturnPending() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "companyName": "测试公司",
                  "position": "测试岗位",
                  "startDate": "2024-07-01",
                  "endDate": "2024-09-01",
                  "location": "测试地点",
                  "description": "测试描述"
                }
                """;
        mockMvc.perform(post("/api/internship/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetPendingList() throws Exception {
        mockMvc.perform(get("/api/internship/pending-list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldApprovePendingRecord() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "companyName": "审批测试公司",
                  "position": "审批测试岗位",
                  "startDate": "2024-07-01",
                  "endDate": "2024-09-01",
                  "location": "杭州",
                  "description": "测试审批流程"
                }
                """;
        String result = mockMvc.perform(post("/api/internship/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(result);
        m.find();
        int id = Integer.parseInt(m.group(1));
        mockMvc.perform(put("/api/internship/approve/" + id)
                        .param("status", "APPROVED")
                        .param("comment", "审批通过-集成测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc.perform(delete("/api/internship/delete/" + id));
    }
}
