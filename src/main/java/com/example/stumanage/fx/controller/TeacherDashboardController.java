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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

/**
 * 教师工作台 — 各模块待审批列表 + 审批操作
 */
public class TeacherDashboardController {

    @FXML private Label userInfoLabel;
    @FXML private VBox contentArea;

    private ApiClient api;
    private String role;

    // 6 个审批模块
    private static final Map<String, String[]> APPROVAL_MODULES = new LinkedHashMap<>();
    static {
        APPROVAL_MODULES.put("honor",              new String[]{"🏅 荣誉", "/api/honor"});
        APPROVAL_MODULES.put("competition",        new String[]{"🏆 学科竞赛", "/api/competition"});
        APPROVAL_MODULES.put("science-achievement",new String[]{"🔬 科技成果", "/api/science-achievement"});
        APPROVAL_MODULES.put("access",             new String[]{"🚪 出入校申请", "/api/access"});
        APPROVAL_MODULES.put("internship",         new String[]{"🏢 校外实习", "/api/internship"});
        APPROVAL_MODULES.put("innovation-project", new String[]{"💡 创新项目", "/api/innovation-project"});
        APPROVAL_MODULES.put("training-lecture",   new String[]{"📚 培训讲座", "/api/training-lecture"});
        APPROVAL_MODULES.put("social-practice",    new String[]{"🌍 社会实践", "/api/social-practice"});
    }

    public void init(ApiClient api, Map<String, Object> loginResult) {
        this.api = api;
        this.role = (String) loginResult.get("role");
        this.teacherPersonId = (Integer) loginResult.get("personId");
        userInfoLabel.setText("👨‍🏫 教师 (ID:" + teacherPersonId + ")");
        // 等 UI 完全渲染后再加载数据
        javafx.application.Platform.runLater(() -> showDefaultPending());
    }

    /** 默认显示第一个模块的待审批列表 */
    private void showDefaultPending() {
        showPendingList("competition");
    }

    /** 显示指定模块的待审批列表 */
    @FXML
    public void showPending(javafx.event.ActionEvent event) {
        String moduleKey = (String) ((Button) event.getSource()).getUserData();
        showPendingList(moduleKey);
    }

    private void showPendingList(String moduleKey) {
        String[] info = APPROVAL_MODULES.get(moduleKey);
        if (info == null) return;

        contentArea.getChildren().clear();

        Label title = new Label(info[0] + " — 待审批列表");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 16 0;");
        contentArea.getChildren().add(title);

        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentArea.getChildren().add(table);

        String apiPath = info[1];

        new Thread(() -> {
            try {
                String resp = api.get(apiPath + "/pending-list");
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> buildPendingTable(table, list, apiPath));
            } catch (Exception e) {
                Platform.runLater(() ->
                        table.setPlaceholder(new Label("加载失败: " + e.toString())));
            }
        }).start();
    }

    /** 构建待审批表格（含审批按钮） */
    private void buildPendingTable(TableView<Map<String, String>> table,
                                   List<Map<String, Object>> list, String apiPath) {
        table.getColumns().clear();
        table.getItems().clear();
        if (list.isEmpty()) {
            table.setPlaceholder(new Label("暂无待审批记录 ✅"));
            return;
        }

        Map<String, Object> first = list.get(0);
        for (String key : first.keySet()) {
            if ("student".equals(key) || "teacherComment".equals(key)) continue;
            TableColumn<Map<String, String>, String> col = new TableColumn<>(key);
            col.setCellValueFactory(data -> {
                String val = data.getValue().get(key);
                return val != null ? new javafx.beans.property.SimpleStringProperty(val) :
                        new javafx.beans.property.SimpleStringProperty("");
            });
            table.getColumns().add(col);
        }

        // 操作列：通过 + 驳回
        TableColumn<Map<String, String>, String> actionCol = new TableColumn<>("操作");
        actionCol.setMinWidth(180);
        actionCol.setCellFactory(c -> new TableCell<>() {
            private final Button approveBtn = new Button("✅ 通过");
            private final Button rejectBtn = new Button("❌ 驳回");
            private final HBox box = new HBox(8, approveBtn, rejectBtn);
            {
                approveBtn.getStyleClass().add("btn-success");
                rejectBtn.getStyleClass().add("btn-danger");
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Map<String, String> row = getTableView().getItems().get(getIndex());
                approveBtn.setOnAction(e -> approve(row.get("id"), "APPROVED", "审核通过", apiPath, table));
                rejectBtn.setOnAction(e -> approve(row.get("id"), "REJECTED", "审核不通过", apiPath, table));
                setGraphic(box);
            }
        });
        table.getColumns().add(actionCol);

        for (Map<String, Object> row : list) {
            Map<String, String> rowStr = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : row.entrySet()) {
                Object v = e.getValue();
                rowStr.put(e.getKey(), v instanceof Map ? "(学生)" : String.valueOf(v));
            }
            table.getItems().add(rowStr);
        }
    }

    /** 执行审批操作 */
    private void approve(String id, String status, String comment,
                         String apiPath, TableView<Map<String, String>> table) {
        try {
            String encStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
            String encComment = URLEncoder.encode(comment, StandardCharsets.UTF_8);
            api.put(apiPath + "/approve/" + id + "?status=" + encStatus + "&comment=" + encComment, "");
            showPendingList(getModuleKey(apiPath));  // 刷新
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "操作失败: " + e.getMessage()).show();
        }
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
                        if (e.getKey().contains("人数")) continue;  // 略过用户统计
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
                Platform.runLater(() -> cards.getChildren().add(
                        new Label("加载失败")));
            }
        }).start();
        contentArea.getChildren().add(cards);
    }

    // ========== 课程管理 ==========

    private Integer teacherPersonId;

    @FXML
    public void showMyCourses() {
        contentArea.getChildren().clear();

        Label title = new Label("📖 我的课程 — 课程管理");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 10 0;");
        contentArea.getChildren().add(title);

        // 新增课程按钮
        Button addBtn = new Button("➕ 发布新课程");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showAddCourseForm());
        contentArea.getChildren().add(addBtn);

        // 课程表格
        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentArea.getChildren().add(table);

        loadMyCourses(table);
    }

    private void loadMyCourses(TableView<Map<String, String>> table) {
        new Thread(() -> {
            try {
                // 教师用 personId 查自己的课程
                String resp = api.get("/api/course/my-list?teacherId=" + teacherPersonId);
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> {
                    table.getColumns().clear();
                    table.getItems().clear();
                    if (list.isEmpty()) {
                        table.setPlaceholder(new Label("暂无课程，点击上方按钮发布"));
                        return;
                    }

                    // 构建列
                    Map<String, Object> first = list.get(0);
                    for (String key : first.keySet()) {
                        if ("teacher".equals(key)) continue;
                        TableColumn<Map<String, String>, String> col = new TableColumn<>(key);
                        col.setCellValueFactory(data -> {
                            String val = data.getValue().get(key);
                            return val != null ? new javafx.beans.property.SimpleStringProperty(val)
                                    : new javafx.beans.property.SimpleStringProperty("");
                        });
                        table.getColumns().add(col);
                    }

                    // 操作列
                    TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
                    actCol.setMinWidth(380);
                    actCol.setCellFactory(c -> new TableCell<>() {
                        private final Button hwBtn = new Button("📝 布置作业");
                        private final Button subBtn = new Button("📂 查看提交");
                        private final Button editBtn = new Button("编辑");
                        private final Button delBtn = new Button("删除");
                        private final HBox box = new HBox(6, hwBtn, subBtn, editBtn, delBtn);
                        {
                            hwBtn.getStyleClass().add("btn-primary");
                            subBtn.getStyleClass().add("btn-success");
                            editBtn.getStyleClass().add("btn-warning");
                            delBtn.getStyleClass().add("btn-danger");
                        }
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) { setGraphic(null); return; }
                            Map<String, String> row = getTableView().getItems().get(getIndex());
                            hwBtn.setOnAction(e -> showAddAssignmentForm(row.get("id")));
                            subBtn.setOnAction(e -> showSubmissions(row.get("id")));
                            editBtn.setOnAction(e -> showEditCourseForm(row, table));
                            delBtn.setOnAction(e -> deleteCourse(row.get("id"), table));
                            setGraphic(box);
                        }
                    });
                    table.getColumns().add(actCol);

                    for (Map<String, Object> row : list) {
                        Map<String, String> rowStr = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : row.entrySet()) {
                            Object v = e.getValue();
                            rowStr.put(e.getKey(), v instanceof Map ? "(教师)" : String.valueOf(v));
                        }
                        table.getItems().add(rowStr);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        table.setPlaceholder(new Label("加载失败: " + e.toString())));
            }
        }).start();
    }

    private void showAddCourseForm() {
        Stage s = new Stage(); s.setTitle("发布新课程");
        VBox form = new VBox(10); form.setPadding(new Insets(20));

        String[] labels = {"课程名称","课程编号","学分","课时","教室","上课时间","学期","描述"};
        Map<String, TextField> fields = new LinkedHashMap<>();
        for (String lb : labels) { Label l = new Label(lb); l.setStyle("-fx-font-weight:bold;"); TextField tf = new TextField(); tf.setPromptText("请输入"+lb); fields.put(lb, tf); form.getChildren().addAll(l, tf); }

        Button submit = new Button("发布"); submit.getStyleClass().add("btn-primary"); submit.setMaxWidth(Double.MAX_VALUE);
        submit.setOnAction(e -> {
            try {
                StringBuilder sb = new StringBuilder("{\"teacher\":{\"id\":").append(teacherPersonId).append("}");
                for (var e2 : fields.entrySet()) sb.append(",\"").append(toCourseKey(e2.getKey())).append("\":\"").append(e2.getValue().getText()).append("\"");
                sb.append("}");
                api.post("/api/course/add", sb.toString());
                s.close(); showMyCourses();
            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "失败: " + ex.getMessage()).show(); }
        });
        form.getChildren().add(submit);
        s.setScene(new Scene(form, 400, 500)); s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); s.show();
    }

    private void showEditCourseForm(Map<String, String> row, TableView<Map<String, String>> table) {
        Stage s = new Stage(); s.setTitle("编辑课程");
        VBox form = new VBox(10); form.setPadding(new Insets(20));

        String[] labels = {"课程名称","课程编号","学分","课时","教室","上课时间","学期","描述"};
        String[] keys = {"courseName","courseCode","credit","hours","classroom","courseTime","semester","description"};
        Map<String, TextField> fields = new LinkedHashMap<>();
        for (int i = 0; i < labels.length; i++) { Label l = new Label(labels[i]); l.setStyle("-fx-font-weight:bold;"); TextField tf = new TextField(row.getOrDefault(keys[i], "")); fields.put(keys[i], tf); form.getChildren().addAll(l, tf); }

        Button submit = new Button("保存修改"); submit.getStyleClass().add("btn-primary"); submit.setMaxWidth(Double.MAX_VALUE);
        submit.setOnAction(e -> {
            try {
                StringBuilder sb = new StringBuilder("{\"id\":").append(row.get("id")).append(",\"teacher\":{\"id\":").append(teacherPersonId).append("}");
                for (var e2 : fields.entrySet()) sb.append(",\"").append(e2.getKey()).append("\":\"").append(e2.getValue().getText()).append("\"");
                sb.append("}");
                api.put("/api/course/update", sb.toString());
                s.close(); showMyCourses();
            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "失败: " + ex.getMessage()).show(); }
        });
        form.getChildren().add(submit);
        s.setScene(new Scene(form, 400, 500)); s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); s.show();
    }

    private void deleteCourse(String id, TableView<Map<String, String>> table) {
        new Alert(Alert.AlertType.CONFIRMATION, "确定删除该课程吗？", ButtonType.YES, ButtonType.NO)
                .showAndWait().ifPresent(r -> { if (r == ButtonType.YES) { try { api.delete("/api/course/delete/" + id); showMyCourses(); } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "失败: " + ex.getMessage()).show(); }} });
    }

    // ========== 作业管理 ==========

    private void showAddAssignmentForm(String courseId) {
        Stage s = new Stage(); s.setTitle("布置作业 - 课程ID:" + courseId);
        VBox form = new VBox(10); form.setPadding(new Insets(20));

        String[] labels = {"作业标题","作业描述","截止日期(如2024-12-31)","总分"};
        String[] keys = {"title","description","deadline","totalScore"};
        Map<String, TextField> fields = new LinkedHashMap<>();
        for (int i = 0; i < labels.length; i++) { Label l = new Label(labels[i]); l.setStyle("-fx-font-weight:bold;"); TextField tf = new TextField(); fields.put(keys[i], tf); form.getChildren().addAll(l, tf); }

        Button submit = new Button("发布作业"); submit.getStyleClass().add("btn-primary"); submit.setMaxWidth(Double.MAX_VALUE);
        submit.setOnAction(e -> {
            try {
                String json = String.format("{\"course\":{\"id\":%s},\"title\":\"%s\",\"description\":\"%s\",\"deadline\":\"%s\",\"totalScore\":\"%s\"}",
                        courseId, fields.get("title").getText(), fields.get("description").getText(),
                        fields.get("deadline").getText(), fields.get("totalScore").getText());
                api.post("/api/assignment/add", json);
                s.close(); showMyCourses();
            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "失败: " + ex.getMessage()).show(); }
        });
        form.getChildren().add(submit);
        s.setScene(new Scene(form, 400, 400)); s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); s.show();
    }

    private void showSubmissions(String courseId) {
        contentArea.getChildren().clear();
        Label title = new Label("📂 作业提交查看 - 课程ID:" + courseId);
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 10 0;");
        contentArea.getChildren().add(title);

        // 加载该课程的所有作业
        TableView<Map<String, String>> hwTable = new TableView<>();
        hwTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contentArea.getChildren().add(hwTable);

        new Thread(() -> {
            try {
                String resp = api.get("/api/assignment/list/" + courseId);
                List<Map<String, Object>> assignments = parseList(resp);
                Platform.runLater(() -> buildAssignmentTable(hwTable, assignments));
            } catch (Exception e) {
                Platform.runLater(() -> hwTable.setPlaceholder(new Label("加载失败: " + e.toString())));
            }
        }).start();
    }

    private void buildAssignmentTable(TableView<Map<String, String>> table, List<Map<String, Object>> assignments) {
        table.getColumns().clear(); table.getItems().clear();
        if (assignments.isEmpty()) { table.setPlaceholder(new Label("该课程暂无作业")); return; }

        Map<String, Object> first = assignments.get(0);
        for (String key : first.keySet()) {
            if ("course".equals(key)) continue;
            TableColumn<Map<String, String>, String> col = new TableColumn<>(key);
            col.setCellValueFactory(d -> {
                String v = d.getValue().get(key);
                return new javafx.beans.property.SimpleStringProperty(v != null ? v : "");
            });
            table.getColumns().add(col);
        }

        // 添加"查看提交"按钮
        TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
        actCol.setMinWidth(160);
        actCol.setCellFactory(c -> new TableCell<>() {
            private final Button viewBtn = new Button("查看提交");
            private final Button gradeBtn = new Button("批改");
            private final HBox box = new HBox(6, viewBtn, gradeBtn);
            { viewBtn.getStyleClass().add("btn-primary"); gradeBtn.getStyleClass().add("btn-warning"); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Map<String, String> row = getTableView().getItems().get(getIndex());
                viewBtn.setOnAction(e -> showSubmissionList(row.get("id")));
                gradeBtn.setOnAction(e -> showGradingForm(row.get("id")));
                setGraphic(box);
            }
        });
        table.getColumns().add(actCol);

        for (Map<String, Object> row : assignments) {
            Map<String, String> rs = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : row.entrySet())
                rs.put(e.getKey(), e.getValue() instanceof Map ? "(课程)" : String.valueOf(e.getValue()));
            table.getItems().add(rs);
        }
    }

    private void showSubmissionList(String assignmentId) {
        Stage s = new Stage(); s.setTitle("提交列表 - 作业ID:" + assignmentId);
        VBox box = new VBox(10); box.setPadding(new Insets(16));
        TableView<Map<String, String>> subTable = new TableView<>();
        subTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(subTable, Priority.ALWAYS);
        box.getChildren().add(subTable);

        new Thread(() -> {
            try {
                String resp = api.get("/api/assignment-submission/list/" + assignmentId);
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> {
                    subTable.getColumns().clear(); subTable.getItems().clear();
                    if (list.isEmpty()) { subTable.setPlaceholder(new Label("暂无提交")); return; }
                    Map<String, Object> f = list.get(0);
                    for (String k : f.keySet()) {
                        if ("assignment".equals(k) || "student".equals(k)) continue;
                        TableColumn<Map<String, String>, String> c = new TableColumn<>(k);
                        c.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                                String.valueOf(d.getValue().getOrDefault(k, ""))));
                        subTable.getColumns().add(c);
                    }
                    for (Map<String, Object> r : list) {
                        Map<String, String> rs = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : r.entrySet())
                            rs.put(e.getKey(), e.getValue() instanceof Map ? "(关联)" : String.valueOf(e.getValue()));
                        subTable.getItems().add(rs);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> subTable.setPlaceholder(new Label("失败: " + ex.toString())));
            }
        }).start();

        s.setScene(new Scene(box, 600, 400));
        s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        s.show();
    }

    private void showGradingForm(String assignmentId) {
        Stage s = new Stage(); s.setTitle("批改 - 作业ID:" + assignmentId);
        VBox box = new VBox(10); box.setPadding(new Insets(16));

        // 查询该作业的所有提交
        new Thread(() -> {
            try {
                String resp = api.get("/api/assignment-submission/list/" + assignmentId);
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> {
                    for (Map<String, Object> sub : list) {
                        String subId = String.valueOf(sub.get("id"));
                        HBox row = new HBox(10);
                        row.setPadding(new Insets(8, 0, 8, 0));
                        Label info = new Label("提交ID:" + subId + " | 内容:" + sub.getOrDefault("content", "无"));
                        TextField scoreField = new TextField(sub.getOrDefault("score", "") + "");
                        scoreField.setPromptText("分数");
                        scoreField.setPrefWidth(60);
                        TextField feedbackField = new TextField(sub.getOrDefault("feedback", "") + "");
                        feedbackField.setPromptText("评语");
                        feedbackField.setPrefWidth(200);
                        Button saveBtn = new Button("保存"); saveBtn.getStyleClass().add("btn-success");
                        saveBtn.setOnAction(ev -> {
                            try {
                                String json = String.format("{\"id\":%s,\"score\":\"%s\",\"feedback\":\"%s\",\"status\":\"已批改\"}",
                                        subId, scoreField.getText(), feedbackField.getText());
                                api.put("/api/assignment-submission/grade", json);
                            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "失败").show(); }
                        });
                        row.getChildren().addAll(info, new Label("分数:"), scoreField, new Label("评语:"), feedbackField, saveBtn);
                        box.getChildren().add(row);
                    }
                });
            } catch (Exception ex) { /* ignore */ }
        }).start();

        s.setScene(new Scene(box, 800, 500));
        s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        s.show();
    }

    private String toCourseKey(String chinese) {
        return switch (chinese) { case "课程名称"->"courseName"; case "课程编号"->"courseCode"; case "学分"->"credit"; case "课时"->"hours"; case "教室"->"classroom"; case "上课时间"->"courseTime"; case "学期"->"semester"; case "描述"->"description"; default -> chinese; };
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

    private String getModuleKey(String apiPath) {
        for (Map.Entry<String, String[]> e : APPROVAL_MODULES.entrySet()) {
            if (e.getValue()[1].equals(apiPath)) return e.getKey();
        }
        return "competition";
    }
}
