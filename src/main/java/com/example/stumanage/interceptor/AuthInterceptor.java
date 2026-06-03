package com.example.stumanage.interceptor;

import com.example.stumanage.annotation.RequireRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 权限拦截器 — 每个请求到达 Controller 之前执行
 * 流程：读取 Session 中的角色 → 对比方法上的 @RequireRole 注解 → 决定放行或拦截
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 0. 不是 Controller 方法（如静态资源），直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 1. 获取方法上的 @RequireRole 注解
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        // 2. 没加注解 = 不需要权限控制，直接放行（兼容老接口）
        if (requireRole == null) {
            return true;
        }

        // 3. 从 Session 中取出当前登录用户的角色（登录时存入）
        HttpSession session = request.getSession(false);   // false: 没有 Session 不新建
        if (session == null || session.getAttribute("role") == null) {
            response.setStatus(401);   // 401 Unauthorized — 还没登录
            response.getWriter().write("{\"error\":\"请先登录\"}");
            return false;
        }

        String currentRole = (String) session.getAttribute("role");

        // 4. 判断当前用户角色是否在允许的列表中
        String[] allowedRoles = requireRole.value();
        boolean hasPermission = Arrays.asList(allowedRoles).contains(currentRole);

        if (!hasPermission) {
            response.setStatus(403);   // 403 Forbidden — 没权限
            response.getWriter().write("{\"error\":\"权限不足\"}");
            return false;
        }

        // 5. 校验通过，放行
        return true;
    }
}
