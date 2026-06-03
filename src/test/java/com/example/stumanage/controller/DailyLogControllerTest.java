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
class DailyLogControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldGetMyList() throws Exception {
        mockMvc.perform(get("/api/daily-log/my-list?studentId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddLog() throws Exception {
        String json = """
                {
                  "student": {"id": 1},
                  "title": "今日学习Java",
                  "type": "学习",
                  "logDate": "2024-10-20",
                  "content": "学习了Spring Boot框架的基础知识，完成了第一个Demo项目",
                  "mood": "开心",
                  "tags": "学习,Java,SpringBoot"
                }
                """;
        mockMvc.perform(post("/api/daily-log/add")
                        .contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("今日学习Java"));
    }

    @Test
    void shouldUpdateLog() throws Exception {
        // 先新增一条
        String addJson = """
                {
                  "student": {"id": 1},
                  "title": "待修改的日志",
                  "type": "生活",
                  "logDate": "2024-10-21",
                  "content": "原始内容",
                  "mood": "一般"
                }
                """;
        String result = mockMvc.perform(post("/api/daily-log/add")
                        .contentType("application/json").content(addJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int id = java.util.regex.Pattern.compile("\"id\":(\\d+)")
                .matcher(result).results().findFirst()
                .map(m -> Integer.parseInt(m.group(1))).orElse(0);

        // 修改
        String updateJson = String.format("""
                {
                  "id": %d,
                  "student": {"id": 1},
                  "title": "修改后的日志",
                  "type": "生活",
                  "logDate": "2024-10-21",
                  "content": "修改后的内容",
                  "mood": "开心"
                }
                """, id);
        mockMvc.perform(put("/api/daily-log/update")
                        .contentType("application/json").content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("修改后的日志"));

        // 清理
        mockMvc.perform(delete("/api/daily-log/delete/" + id));
    }
}
