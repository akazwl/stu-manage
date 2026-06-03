package com.example.stumanage.controller;

import com.example.stumanage.model.User;
import com.example.stumanage.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest,
                                     HttpSession session) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String role = loginRequest.get("role");
        User user = userService.authenticate(username, password, role);
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            int pid = user.getPersonId() != null ? user.getPersonId() : user.getId();
            session.setAttribute("personId", pid);
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());
            response.put("success", true);
            response.put("userId", user.getId());
            response.put("personId", pid);
            response.put("role", user.getRole());
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
        }
        return response;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("success", true, "message", "已退出登录");
    }
}
