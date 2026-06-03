package com.example.stumanage.annotation;

import java.lang.annotation.*;

/**
 * 角色权限注解 — 标记在 Controller 方法上，指定哪些角色可以访问
 * 使用示例：
 *   @RequireRole("ADMIN")                    // 仅管理员
 *   @RequireRole({"TEACHER", "ADMIN"})       // 教师或管理员
 *   @RequireRole({"STUDENT", "TEACHER", "ADMIN"})  // 所有登录用户
 */
@Target(ElementType.METHOD)           // 只能用在方法上
@Retention(RetentionPolicy.RUNTIME)   // 运行时保留，反射才能读到
public @interface RequireRole {
    String[] value();                  // 允许的角色列表
}
