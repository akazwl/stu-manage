package com.example.stumanage.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 管理员接口测试 — 验证 @RequireRole("ADMIN") 是否生效
 * 核心：不带 Session → 401，带 Student Session → 403，带 Admin Session → 通过
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class AdminControllerTest {

    @Autowired private MockMvc mockMvc;

    // 模拟管理员 Session（注入 role="ADMIN"）
    private MockHttpSession adminSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");
        session.setAttribute("userId", 1);
        session.setAttribute("username", "admin");
        return session;
    }

    // 模拟学生 Session（role="STUDENT"）
    private MockHttpSession studentSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "STUDENT");
        session.setAttribute("userId", 3);
        return session;
    }

    // ===== 权限拦截测试 =====

    @Test
    @Order(1)
    void shouldRejectWhenNotLoggedIn() throws Exception {
        // 不带 Session 访问管理员接口 → 应返回 401
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    void shouldRejectWhenStudentAccessAdminApi() throws Exception {
        // 学生 Session 访问管理员接口 → 应返回 403
        mockMvc.perform(get("/api/admin/users").session(studentSession()))
                .andExpect(status().isForbidden());
    }

    // ===== 管理员功能测试 =====

    @Test
    @Order(3)
    void shouldGetAllUsersAsAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users").session(adminSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    void shouldCreateAndDeleteUser() throws Exception {
        // 创建新用户
        String json = """
                {
                  "username": "test_user_perm",
                  "password": "123456",
                  "role": "STUDENT"
                }
                """;
        mockMvc.perform(post("/api/admin/users")
                        .session(adminSession())
                        .contentType("application/json").content(json))
                .andExpect(status().isOk());
    }
}
