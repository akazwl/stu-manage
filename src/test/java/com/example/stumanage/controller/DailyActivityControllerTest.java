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
class DailyActivityControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/daily-activity/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddActivity() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "activityName": "班级篮球友谊赛",
                  "type": "体育",
                  "activityDate": "2024-10-15",
                  "location": "学校篮球场",
                  "role": "参与者",
                  "duration": "2",
                  "description": "与2班进行的友谊比赛"
                }
                """;
        mockMvc.perform(post("/api/daily-activity/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityName").value("班级篮球友谊赛"));
    }
}
