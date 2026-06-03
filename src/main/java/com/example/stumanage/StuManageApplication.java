package com.example.stumanage;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 山东大学软件学院 - 学生综合管理系统
 * 后端入口：启动 Spring Boot REST API（端口 22222）
 */
@SpringBootApplication
public class StuManageApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 后端
        SpringApplication.run(StuManageApplication.class, args);
    }
}
