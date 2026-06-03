package com.example.stumanage.controller;

import com.example.stumanage.annotation.RequireRole;
import com.example.stumanage.model.User;
import com.example.stumanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员接口 — 用户账号的增删改查
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequireRole("ADMIN")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequireRole("ADMIN")
    @GetMapping("/users/role/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }

    /**
     * 管理员创建新账号 — Person 在 UserService 中自动创建
     */
    @RequireRole("ADMIN")
    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            return Map.of("success", true, "message", "用户创建成功", "userId", created.getId());
        } catch (RuntimeException e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @RequireRole("ADMIN")
    @PutMapping("/users/{id}/reset-password")
    public Map<String, Object> resetPassword(@PathVariable Integer id,
                                             @RequestBody Map<String, String> body) {
        try {
            userService.resetPassword(id, body.get("newPassword"));
            return Map.of("success", true, "message", "密码重置成功");
        } catch (RuntimeException e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @RequireRole("ADMIN")
    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return Map.of("success", true, "message", "用户已删除");
        } catch (RuntimeException e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}
