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
class StatisticsControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyDashboard() throws Exception {
        mockMvc.perform(get("/api/statistics/my-dashboard?studentId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.竞赛").isNumber());
    }

    @Test
    void shouldGetOverview() throws Exception {
        mockMvc.perform(get("/api/statistics/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.竞赛总数").isNumber())
                .andExpect(jsonPath("$.管理员人数").isNumber());
    }
}
