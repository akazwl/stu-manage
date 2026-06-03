package com.example.stumanage.fx.controller;

import com.example.stumanage.fx.service.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

/**
 * 管理员控制台 — 用户管理 + 系统总览
 */
public class AdminDashboardController {

    @FXML private Label userInfoLabel;
    @FXML private VBox contentArea;

    private ApiClient api;

    public void init(ApiClient api, Map<String, Object> loginResult) {
        this.api = api;
        userInfoLabel.setText("🛡 管理员 (ID:" + loginResult.get("personId") + ")");
        showUserManagement();  // 默认显示用户管理
    }

    /** 用户管理 */
    @FXML
    public void showUserManagement() {
        contentArea.getChildren().clear();
        Label title = new Label("👥 用户管理");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 10 0;");
        contentArea.getChildren().add(title);

        // 新增用户表单
        HBox addBar = new HBox(10);
        addBar.setPadding(new Insets(0, 0, 16, 0));
        TextField newUsername = new TextField();
        newUsername.setPromptText("用户名");
        newUsername.getStyleClass().add("input-field");
        TextField newPassword = new TextField();
        newPassword.setPromptText("密码");
        newPassword.getStyleClass().add("input-field");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("STUDENT", "TEACHER", "ADMIN");
        roleBox.setValue("STUDENT");
        roleBox.getStyleClass().add("input-field");
        Button createBtn = new Button("➕ 创建用户");
        createBtn.getStyleClass().add("btn-primary");
        addBar.getChildren().addAll(newUsername, newPassword, roleBox, createBtn);

        // 用户表格
        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        contentArea.getChildren().addAll(addBar, table);

        // 加载用户列表
        loadUsers(table);

        // 创建用户事件
        createBtn.setOnAction(e -> {
            try {
                String json = String.format(
                        "{\"username\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                        newUsername.getText(), newPassword.getText(), roleBox.getValue());
                String resp = api.post("/api/admin/users", json);
                @SuppressWarnings("unchecked")
                Map<String, Object> result = (Map<String, Object>) api.parse(resp);
                if (Boolean.TRUE.equals(result.get("success"))) {
                    new Alert(Alert.AlertType.INFORMATION, "用户创建成功！").show();
                    newUsername.clear();
                    newPassword.clear();
                    loadUsers(table);
                } else {
                    new Alert(Alert.AlertType.ERROR, "创建失败: " + result.get("message")).show();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "创建失败: " + ex.getMessage()).show();
            }
        });
    }

    private void loadUsers(TableView<Map<String, String>> table) {
        new Thread(() -> {
            try {
                String resp = api.get("/api/admin/users");
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> {
                    table.getColumns().clear();
                    table.getItems().clear();

                    if (list.isEmpty()) return;

                    Map<String, Object> first = list.get(0);
                    for (String key : first.keySet()) {
                        if ("person".equals(key)) continue;
                        TableColumn<Map<String, String>, String> col = new TableColumn<>(key);
                        col.setCellValueFactory(data -> {
                            String val = data.getValue().get(key);
                            return val != null ? new javafx.beans.property.SimpleStringProperty(val) :
                                    new javafx.beans.property.SimpleStringProperty("");
                        });
                        table.getColumns().add(col);
                    }

                    // 操作列
                    TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
                    actCol.setMinWidth(200);
                    actCol.setCellFactory(c -> new TableCell<>() {
                        private final Button resetBtn = new Button("重置密码");
                        private final Button delBtn = new Button("删除");
                        private final HBox box = new HBox(8, resetBtn, delBtn);
                        {
                            resetBtn.getStyleClass().add("btn-warning");
                            delBtn.getStyleClass().add("btn-danger");
                        }
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) { setGraphic(null); return; }
                            Map<String, String> row = getTableView().getItems().get(getIndex());
                            resetBtn.setOnAction(e -> resetPassword(row.get("id")));
                            delBtn.setOnAction(e -> deleteUser(row.get("id"), table));
                            setGraphic(box);
                        }
                    });
                    table.getColumns().add(actCol);

                    for (Map<String, Object> row : list) {
                        Map<String, String> rowStr = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : row.entrySet()) {
                            Object v = e.getValue();
                            if ("password".equals(e.getKey())) {
                                rowStr.put(e.getKey(), "********");  // 密码脱敏
                            } else {
                                rowStr.put(e.getKey(), v instanceof Map ? "(关联)" : String.valueOf(v));
                            }
                        }
                        table.getItems().add(rowStr);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> table.setPlaceholder(
                        new Label("加载失败: " + e.getMessage())));
            }
        }).start();
    }

    private void resetPassword(String userId) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("重置密码");
        dialog.setHeaderText("为用户 ID=" + userId + " 设置新密码\n输入新密码后点击确定");
        dialog.setContentText("新密码:");
        dialog.showAndWait().ifPresent(newPwd -> {
            try {
                api.put("/api/admin/users/" + userId + "/reset-password",
                        "{\"newPassword\":\"" + newPwd + "\"}");
                new Alert(Alert.AlertType.INFORMATION, "密码已重置").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "重置失败: " + e.getMessage()).show();
            }
        });
    }

    private void deleteUser(String userId, TableView<Map<String, String>> table) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "确定要删除用户 " + userId + " 吗？", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                try {
                    api.delete("/api/admin/users/" + userId);
                    loadUsers(table);
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "删除失败: " + e.getMessage()).show();
                }
            }
        });
    }

    /** 系统总览 */
    @FXML
    public void showOverview() {
        contentArea.getChildren().clear();
        Label title = new Label("📊 系统数据总览");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 16 0;");
        contentArea.getChildren().add(title);

        FlowPane cards = new FlowPane(16, 16);
        new Thread(() -> {
            try {
                String resp = api.get("/api/statistics/overview");
                Map<String, Object> stats = api.parseMap(resp);
                Platform.runLater(() -> {
                    for (Map.Entry<String, Object> e : stats.entrySet()) {
                        VBox card = new VBox(8);
                        card.getStyleClass().add("stat-card");
                        Label num = new Label(String.valueOf(e.getValue()));
                        num.getStyleClass().add("stat-number");
                        Label lbl = new Label(e.getKey());
                        lbl.getStyleClass().add("stat-label");
                        card.getChildren().addAll(num, lbl);
                        cards.getChildren().add(card);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> cards.getChildren().add(new Label("加载失败")));
            }
        }).start();
        contentArea.getChildren().add(cards);
    }

    /** 待审批列表（复用教师端逻辑的思路，快速查看） */
    @FXML
    public void showPendingApprovals() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Label("待审批列表：请切换到教师工作台操作",
                null) {{
            setStyle("-fx-font-size:14px; -fx-text-fill:#666;");
        }});
    }

    @FXML
    public void handleLogout() {
        try {
            api.post("/api/auth/logout", "{}");
            api.clearSession();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("登录");
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseList(String json) throws Exception {
        Object parsed = api.parse(json);
        if (parsed instanceof List) return (List<Map<String, Object>>) parsed;
        return List.of();
    }
}
