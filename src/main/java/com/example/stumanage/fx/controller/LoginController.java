package com.example.stumanage.fx.controller;

import com.example.stumanage.fx.service.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Map;

/**
 * 登录页控制器 — 验证用户名密码角色，成功后跳转到对应工作台
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ToggleButton btnAdmin, btnTeacher, btnStudent;
    @FXML private ToggleGroup roleGroup;
    @FXML private Label messageLabel;

    private final ApiClient api = new ApiClient();

    @FXML
    public void initialize() {
        // 初始化 ToggleGroup
        btnAdmin.setToggleGroup(roleGroup);
        btnTeacher.setToggleGroup(roleGroup);
        btnStudent.setToggleGroup(roleGroup);
        btnStudent.setSelected(true);  // 默认选学生
    }

    /** 点击登录按钮 */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = getSelectedRole();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠ 请输入用户名和密码");
            return;
        }

        try {
            // 调用后端登录接口 POST /api/auth/login
            String body = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                    username, password, role);
            String resp = api.post("/api/auth/login", body);
            Map<String, Object> result = api.parseMap(resp);

            if (Boolean.TRUE.equals(result.get("success"))) {
                // 登录成功 → 根据角色跳转到不同页面
                String fxmlFile = switch (role) {
                    case "ADMIN"   -> "/fxml/admin-dashboard.fxml";
                    case "TEACHER" -> "/fxml/teacher-dashboard.fxml";
                    default        -> "/fxml/student-dashboard.fxml";
                };

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();

                // 把 ApiClient 和用户信息传给下一个控制器
                Object controller = loader.getController();
                if (controller instanceof StudentDashboardController c) {
                    c.init(api, result);
                } else if (controller instanceof TeacherDashboardController c) {
                    c.init(api, result);
                } else if (controller instanceof AdminDashboardController c) {
                    c.init(api, result);
                }

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 1200, 750));
                stage.setTitle("学生综合管理系统 - 山东大学软件学院");
                stage.centerOnScreen();
            } else {
                messageLabel.setText("❌ " + result.getOrDefault("message", "登录失败"));
            }
        } catch (Exception e) {
            messageLabel.setText("❌ 连接服务器失败，请确认后端已启动");
            e.printStackTrace();
        }
    }

    private String getSelectedRole() {
        ToggleButton selected = (ToggleButton) roleGroup.getSelectedToggle();
        if (selected == btnAdmin) return "ADMIN";
        if (selected == btnTeacher) return "TEACHER";
        return "STUDENT";
    }
}
