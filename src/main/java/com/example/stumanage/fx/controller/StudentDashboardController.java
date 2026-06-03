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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 学生工作台 — 支持全部 17 个功能模块
 */
public class StudentDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;
    @FXML private VBox contentArea;

    private ApiClient api;
    private Integer personId;   // 登录返回的 personId
    private Integer studentId;  // 兼容用

    // ========== 全部模块定义 ==========
    // 格式: key → {显示名, api路径, 列表URL后缀, 参数传递方式, 参数名, 是否审批模块}
    private static final List<ModuleDef> ALL_MODULES = List.of(
        // 基础信息
        new ModuleDef("person",           "👤 个人信息",    "/api/person",          "/%d",       "path",   "id",       false),
        new ModuleDef("family",           "👨‍👩‍👧 家庭信息",  "/api/family",          "/my-list?personId=%d", "query", "personId", false),
        new ModuleDef("pre-enrollment",   "📝 入学前信息",   "/api/pre-enrollment",  "/my-info?personId=%d", "query", "personId", false),
        new ModuleDef("social-relation",  "🔗 社会关系",    "/api/social-relation", "/my-list?personId=%d", "query", "personId", false),
        // 奖惩出入
        new ModuleDef("honor",            "🏅 荣誉",        "/api/honor",           "/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("competition",      "🏆 学科竞赛",    "/api/competition",     "/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("science-achievement","🔬 科技成果",  "/api/science-achievement","/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("access",           "🚪 出入校申请",  "/api/access",          "/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("internship",       "🏢 校外实习",    "/api/internship",      "/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("social-practice",  "🌍 社会实践",    "/api/social-practice", "/my-list?studentId=%d","query","studentId", true),
        // 项目培训
        new ModuleDef("innovation-project","💡 创新项目",   "/api/innovation-project","/my-list?studentId=%d","query","studentId", true),
        new ModuleDef("training-lecture", "📚 培训讲座",    "/api/training-lecture","/my-list?studentId=%d","query","studentId", true),
        // 教学教务
        new ModuleDef("course",           "📖 课程",        "/api/course",          "/all",      "none",   "",         false),
        new ModuleDef("course-selection", "📋 选课",        "/api/course-selection","/my-list?studentId=%d","query","studentId",false),
        new ModuleDef("attendance",       "📌 考勤",        "/api/attendance",      "/my-list?studentId=%d","query","studentId",false),
        new ModuleDef("assignment",       "✏️ 作业",        "/api/assignment",      "/list/%d",  "path",   "courseId", false),
        // 校园生活
        new ModuleDef("daily-activity",   "🏃 日常活动",    "/api/daily-activity",  "/my-list?studentId=%d","query","studentId", false),
        new ModuleDef("daily-log",        "📝 日志记录",    "/api/daily-log",       "/my-list?studentId=%d","query","studentId", false)
    );

    /** 由 LoginController 调用 */
    public void init(ApiClient api, Map<String, Object> loginResult) {
        this.api = api;
        this.personId = (Integer) loginResult.get("personId");
        this.studentId = personId;  // API 中 studentId 实际指向 Person ID
        welcomeLabel.setText("欢迎，同学");
        userInfoLabel.setText("👤 学生 (ID:" + personId + ")");
        showDashboard();
    }

    // ========== 仪表盘 ==========

    @FXML
    public void showDashboard() {
        contentArea.getChildren().clear();
        Label title = new Label("📋 我的仪表盘");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 12 0;");
        contentArea.getChildren().add(title);

        FlowPane cards = new FlowPane(16, 16);
        cards.setPadding(new Insets(0));

        for (ModuleDef mod : ALL_MODULES) {
            VBox card = new VBox(8);
            card.getStyleClass().addAll("module-card", getCardStyle(mod.key));
            card.setPadding(new Insets(16));

            Label icon = new Label(mod.name.substring(0, 2));
            icon.setStyle("-fx-font-size:24px;");
            Label name = new Label(mod.name.substring(3));
            name.getStyleClass().add("module-card-name");
            Label count = new Label("...");
            count.getStyleClass().add("module-card-count");
            Label unit = new Label("条记录");
            unit.getStyleClass().add("module-card-unit");

            card.getChildren().addAll(icon, name, count, unit);
            card.setOnMouseClicked(e -> showModule(mod));
            cards.getChildren().add(card);

            // 后台加载计数
            new Thread(() -> {
                try {
                    String url = buildLoadUrl(mod);
                    String resp;
                    if ("body".equals(mod.paramStyle)) {
                        resp = api.getWithBody(url, getParamValue(mod).toString());
                    } else {
                        resp = api.get(url);
                    }
                    int size = parseCount(resp);
                    Platform.runLater(() -> count.setText(String.valueOf(size)));
                } catch (Exception e) {
                    Platform.runLater(() -> count.setText("?"));
                }
            }).start();
        }
        contentArea.getChildren().add(cards);
    }

    // ========== 模块列表页 ==========

    @FXML
    public void showModule(javafx.event.ActionEvent event) {
        String key = (String) ((Button) event.getSource()).getUserData();
        for (ModuleDef m : ALL_MODULES) {
            if (m.key.equals(key)) { showModule(m); return; }
        }
    }

    private void showModule(ModuleDef mod) {
        // 作业模块特殊处理：先让学生选课程
        if ("assignment".equals(mod.key)) {
            showAssignmentCourses();
            return;
        }
        contentArea.getChildren().clear();

        Label title = new Label(mod.name);
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 12 0;");
        contentArea.getChildren().add(title);

        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentArea.getChildren().add(table);

        // 新增 + 编辑按钮
        boolean canAdd = !"none".equals(mod.paramStyle);
        boolean canEdit = List.of("person","family","social-relation","pre-enrollment","access","daily-log").contains(mod.key);
        if (canAdd || canEdit) {
            HBox btnBar = new HBox(10);
            if (canAdd) {
                Button addBtn = new Button("➕ 新增记录");
                addBtn.getStyleClass().add("btn-primary");
                addBtn.setOnAction(e -> showAddForm(mod, table));
                btnBar.getChildren().add(addBtn);
            }
            if (canEdit) {
                Button editBtn = new Button("✏️ 编辑选中");
                editBtn.getStyleClass().add("btn-warning");
                editBtn.setOnAction(e -> {
                    Map<String, String> selected = table.getSelectionModel().getSelectedItem();
                    if (selected != null) showEditForm(mod, selected, table);
                    else new Alert(Alert.AlertType.WARNING, "请先在表格中选中一条记录").show();
                });
                btnBar.getChildren().add(editBtn);
            }
            contentArea.getChildren().add(btnBar);
        }

        loadModuleData(mod, table);
    }

    private void loadModuleData(ModuleDef mod, TableView<Map<String, String>> table) {
        new Thread(() -> {
            try {
                String url = buildLoadUrl(mod);
                String resp;
                if ("body".equals(mod.paramStyle)) {
                    resp = api.getWithBody(url, getParamValue(mod).toString());
                } else {
                    resp = api.get(url);
                }
                List<Map<String, Object>> list = parseList(resp);
                Platform.runLater(() -> fillTable(table, list, mod));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    table.getColumns().clear();
                    table.setPlaceholder(new Label("加载失败: " + e.getMessage()));
                });
            }
        }).start();
    }

    private String buildLoadUrl(ModuleDef mod) {
        if ("path".equals(mod.paramStyle)) {
            return mod.apiPath + String.format(mod.listSuffix, personId);
        } else if ("query".equals(mod.paramStyle)) {
            return mod.apiPath + String.format(mod.listSuffix, getParamValue(mod));
        } else {
            return mod.apiPath + mod.listSuffix; // "none" — like /all
        }
    }

    private Integer getParamValue(ModuleDef mod) {
        // 后端 API 的 studentId/personId 都指向 Person 表的 id
        return personId;
    }

    // ========== 表格填充 ==========

    private void fillTable(TableView<Map<String, String>> table,
                           List<Map<String, Object>> list, ModuleDef mod) {
        table.getColumns().clear();
        table.getItems().clear();
        if (list.isEmpty()) {
            table.setPlaceholder(new Label("暂无数据"));
            return;
        }

        Map<String, Object> first = list.get(0);
        for (String key : first.keySet()) {
            if (key.equals("student") || key.equals("person") || key.equals("teacher")
                || key.equals("course") || key.equals("assignment") || key.equals("teacherComment")
                || key.equals("password"))
                continue;

            TableColumn<Map<String, String>, String> col = new TableColumn<>(key);
            col.setCellValueFactory(data -> {
                String val = data.getValue().get(key);
                return val != null ? new javafx.beans.property.SimpleStringProperty(val)
                        : new javafx.beans.property.SimpleStringProperty("");
            });
            if ("status".equals(key)) {
                col.setCellFactory(c -> new TableCell<>() {
                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) { setText(""); return; }
                        setText(item);
                        getStyleClass().clear();
                        if (item.contains("PENDING")) getStyleClass().add("status-pending");
                        else if (item.contains("APPROVED")||item.contains("出勤")) getStyleClass().add("status-approved");
                        else if (item.contains("REJECTED")||item.contains("缺勤")||item.contains("迟到")) getStyleClass().add("status-rejected");
                    }
                });
            }
            table.getColumns().add(col);
        }

        // 操作列 — 选课（仅课程模块）/ 删除按钮
        if (!"none".equals(mod.paramStyle) || mod.key.equals("course")) {
            TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
            actCol.setMinWidth(mod.key.equals("course") ? 160 : 80);
            actCol.setCellFactory(c -> new TableCell<>() {
                private final HBox box = new HBox(8);
                private final Button selectBtn = new Button("📋 选课");
                private final Button delBtn = new Button("删除");
                {
                    selectBtn.getStyleClass().add("btn-success");
                    delBtn.getStyleClass().add("btn-danger");
                }
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); return; }
                    Map<String, String> row = getTableView().getItems().get(getIndex());
                    box.getChildren().clear();
                    if (mod.key.equals("course")) {
                        // 课程模块：选课按钮
                        selectBtn.setOnAction(e -> selectCourse(row, table));
                        box.getChildren().add(selectBtn);
                    }
                    delBtn.setOnAction(e -> deleteRecord(mod, row.get("id"), table));
                    box.getChildren().add(delBtn);
                    setGraphic(box);
                }
            });
            table.getColumns().add(actCol);
        }

        for (Map<String, Object> row : list) {
            Map<String, String> rowStr = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : row.entrySet()) {
                Object v = e.getValue();
                rowStr.put(e.getKey(), v instanceof Map ? "(关联)" : String.valueOf(v));
            }
            table.getItems().add(rowStr);
        }
    }

    /** 选课：把当前课程加入学生的课表 */
    private void selectCourse(Map<String, String> courseRow, TableView<Map<String, String>> table) {
        try {
            String json = String.format(
                    "{\"course\":{\"id\":%s},\"student\":{\"id\":%d},\"status\":\"已选\",\"selectTime\":\"%s\"}",
                    courseRow.get("id"), personId, java.time.LocalDate.now().toString());
            api.post("/api/course-selection/add", json);
            new Alert(Alert.AlertType.INFORMATION, "选课成功！\n课程：" + courseRow.getOrDefault("courseName", "")).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "选课失败: " + e.getMessage()).show();
        }
    }

    private void deleteRecord(ModuleDef mod, String id, TableView<Map<String, String>> table) {
        try {
            api.delete(mod.apiPath + "/delete/" + id);
            loadModuleData(mod, table);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "删除失败: " + ex.getMessage()).show();
        }
    }

    // ========== 新增表单 ==========

    private void showAddForm(ModuleDef mod, TableView<Map<String, String>> table) {
        Stage formStage = new Stage();
        formStage.setTitle("新增 - " + mod.name);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        Map<String, TextField> fieldMap = new LinkedHashMap<>();
        String[] fields = getFields(mod.key);
        for (String field : fields) {
            Label label = new Label(field);
            label.setStyle("-fx-font-weight:bold; -fx-font-size:12px;");
            TextField tf = new TextField();
            tf.setPromptText("请输入" + field);
            tf.setStyle("-fx-background-radius:6; -fx-padding:8 12;");
            fieldMap.put(field, tf);
            form.getChildren().addAll(label, tf);
        }

        Button submitBtn = new Button("提交");
        submitBtn.getStyleClass().add("btn-primary");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> {
            try {
                StringBuilder sb = new StringBuilder("{");
                // 关联字段
                if (mod.apiPath.contains("course-selection") || mod.apiPath.contains("attendance")
                    || mod.apiPath.contains("assignment")) {
                    sb.append("\"student\":{\"id\":").append(personId).append("},");
                } else if (mod.apiPath.contains("/api/family") || mod.apiPath.contains("/api/social-relation")
                    || mod.apiPath.contains("/api/pre-enrollment")) {
                    sb.append("\"person\":{\"id\":").append(personId).append("},");
                } else {
                    sb.append("\"student\":{\"id\":").append(personId).append("},");
                }

                boolean first = true;
                for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                    String jsonKey = toJsonKey(entry.getKey());
                    if (!first || sb.charAt(sb.length()-1) != ',') sb.append(",");
                    sb.append("\"").append(jsonKey).append("\":\"")
                      .append(entry.getValue().getText()).append("\"");
                }
                // remove trailing comma if needed - just close properly
                sb.append("}");
                String json = sb.toString().replace(",}", "}"); // fix trailing comma

                api.post(mod.apiPath + "/add", json);
                formStage.close();
                loadModuleData(mod, table);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "提交失败: " + ex.getMessage()).show();
            }
        });
        form.getChildren().add(submitBtn);

        formStage.setScene(new Scene(form, 450, 500));
        formStage.getScene().getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm());
        formStage.show();
    }

    // ========== 编辑表单 ==========

    private void showEditForm(ModuleDef mod, Map<String, String> existingData,
                              TableView<Map<String, String>> table) {
        Stage formStage = new Stage();
        formStage.setTitle("编辑 - " + mod.name);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        Map<String, TextField> fieldMap = new LinkedHashMap<>();
        String[] fields = getFields(mod.key);
        for (String field : fields) {
            Label label = new Label(field);
            label.setStyle("-fx-font-weight:bold; -fx-font-size:12px;");
            TextField tf = new TextField();
            // 预填现有值：中文字段名 → JSON key → 查找 existingData
            String jsonKey = toJsonKey(field);
            String currentVal = existingData.getOrDefault(jsonKey, "");
            if (currentVal.equals("null") || currentVal.equals("(关联)")) currentVal = "";
            tf.setText(currentVal);
            tf.setStyle("-fx-background-radius:6; -fx-padding:8 12;");
            fieldMap.put(field, tf);
            form.getChildren().addAll(label, tf);
        }

        Button submitBtn = new Button("保存修改");
        submitBtn.getStyleClass().add("btn-primary");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> {
            try {
                StringBuilder sb = new StringBuilder("{");
                // 带上 id
                sb.append("\"id\":").append(existingData.get("id")).append(",");
                // 关联字段
                if (mod.apiPath.contains("/api/family") || mod.apiPath.contains("/api/social-relation")
                    || mod.apiPath.contains("/api/pre-enrollment")) {
                    sb.append("\"person\":{\"id\":").append(personId).append("},");
                } else if (mod.apiPath.contains("/api/person")) {
                    // person 更新不需要额外关联
                } else {
                    sb.append("\"student\":{\"id\":").append(personId).append("},");
                }
                for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                    sb.append("\"").append(toJsonKey(entry.getKey())).append("\":\"")
                      .append(entry.getValue().getText()).append("\",");
                }
                String json = sb.toString();
                json = json.replaceAll(",$", ""); // 去掉末尾逗号
                json += "}";

                // 使用 update 接口
                api.put(mod.apiPath + "/update", json);
                formStage.close();
                loadModuleData(mod, table);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "保存失败: " + ex.getMessage()).show();
            }
        });
        form.getChildren().add(submitBtn);

        formStage.setScene(new Scene(form, 450, 500));
        formStage.getScene().getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm());
        formStage.show();
    }

    // ========== 作业：先选课 → 看作业 → 提交 ==========

    private void showAssignmentCourses() {
        contentArea.getChildren().clear();
        Label title = new Label("✏️ 作业 — 请选择课程");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 12 0;");
        contentArea.getChildren().add(title);

        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentArea.getChildren().add(table);

        new Thread(() -> {
            try {
                // 加载学生的选课记录
                String resp = api.get("/api/course-selection/my-list?studentId=" + personId);
                List<Map<String, Object>> selections = parseList(resp);
                Platform.runLater(() -> {
                    table.getColumns().clear(); table.getItems().clear();
                    if (selections.isEmpty()) {
                        table.setPlaceholder(new Label("你还没有选课，请先到「选课」模块选课"));
                        return;
                    }
                    // 提取课程信息并去重
                    Set<String> seenCourses = new HashSet<>();
                    List<Map<String, String>> courseList = new ArrayList<>();
                    for (Map<String, Object> sel : selections) {
                        Object courseObj = sel.get("course");
                        if (courseObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> course = (Map<String, Object>) courseObj;
                            String cid = String.valueOf(course.get("id"));
                            if (seenCourses.add(cid)) {
                                Map<String, String> cr = new LinkedHashMap<>();
                                cr.put("id", cid);
                                cr.put("courseName", String.valueOf(course.getOrDefault("courseName", "未知")));
                                cr.put("courseCode", String.valueOf(course.getOrDefault("courseCode", "")));
                                courseList.add(cr);
                            }
                        }
                    }
                    if (courseList.isEmpty()) { table.setPlaceholder(new Label("暂无课程数据")); return; }

                    TableColumn<Map<String, String>, String> nameCol = new TableColumn<>("courseName");
                    nameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().get("courseName")));
                    TableColumn<Map<String, String>, String> codeCol = new TableColumn<>("courseCode");
                    codeCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().get("courseCode")));
                    TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
                    actCol.setCellFactory(c -> new TableCell<>() {
                        private final Button btn = new Button("📝 查看作业");
                        { btn.getStyleClass().add("btn-primary"); }
                        @Override protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) { setGraphic(null); return; }
                            btn.setOnAction(e -> showAssignmentsForCourse(getTableView().getItems().get(getIndex()).get("id")));
                            setGraphic(btn);
                        }
                    });
                    table.getColumns().addAll(nameCol, codeCol, actCol);
                    table.getItems().addAll(courseList);
                });
            } catch (Exception e) {
                Platform.runLater(() -> table.setPlaceholder(new Label("加载失败: " + e.toString())));
            }
        }).start();
    }

    private void showAssignmentsForCourse(String courseId) {
        contentArea.getChildren().clear();
        Label title = new Label("✏️ 课程作业 — 课程ID:" + courseId);
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 12 0;");

        Button backBtn = new Button("← 返回课程列表");
        backBtn.getStyleClass().add("btn-warning");
        backBtn.setOnAction(e -> showAssignmentCourses());

        contentArea.getChildren().addAll(title, backBtn);

        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentArea.getChildren().add(table);

        new Thread(() -> {
            try {
                String resp = api.get("/api/assignment/list/" + courseId);
                List<Map<String, Object>> assignments = parseList(resp);
                final String cid = courseId;
                Platform.runLater(() -> {
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

                    // 提交按钮列
                    TableColumn<Map<String, String>, String> actCol = new TableColumn<>("操作");
                    actCol.setMinWidth(160);
                    actCol.setCellFactory(c -> new TableCell<>() {
                        private final Button submitBtn = new Button("📤 提交作业");
                        private final Button mySubBtn = new Button("📋 我的提交");
                        private final HBox box = new HBox(6, submitBtn, mySubBtn);
                        { submitBtn.getStyleClass().add("btn-success"); mySubBtn.getStyleClass().add("btn-primary"); }
                        @Override protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) { setGraphic(null); return; }
                            Map<String, String> row = getTableView().getItems().get(getIndex());
                            submitBtn.setOnAction(e -> showSubmitForm(row.get("id"), cid));
                            mySubBtn.setOnAction(e -> showMySubmissions(row.get("id")));
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
                });
            } catch (Exception e) {
                Platform.runLater(() -> table.setPlaceholder(new Label("加载失败: " + e.toString())));
            }
        }).start();
    }

    private void showSubmitForm(String assignmentId, String courseId) {
        Stage s = new Stage(); s.setTitle("提交作业 - 作业ID:" + assignmentId);
        VBox form = new VBox(10); form.setPadding(new Insets(20));
        Label l = new Label("作业内容:"); l.setStyle("-fx-font-weight:bold;");
        TextArea contentArea2 = new TextArea(); contentArea2.setPromptText("在此输入你的作业内容，或点击下方按钮选择文件...");
        contentArea2.setPrefRowCount(10);

        // 文件选择按钮
        Button fileBtn = new Button("📂 选择文件（.txt）");
        fileBtn.getStyleClass().add("btn-primary");
        Label fileLabel = new Label("未选择文件");
        fileLabel.setStyle("-fx-font-size:11px; -fx-text-fill:#999;");
        fileBtn.setOnAction(ev -> {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("选择作业文件");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("文本文件", "*.txt"));
            java.io.File f = fc.showOpenDialog(s);
            if (f != null) {
                try {
                    String fileContent = java.nio.file.Files.readString(f.toPath());
                    contentArea2.setText(fileContent);
                    fileLabel.setText("已加载: " + f.getName() + " (" + f.length() + " 字节)");
                } catch (Exception ex) { fileLabel.setText("读取失败: " + ex.getMessage()); }
            }
        });

        HBox fileBar = new HBox(10, fileBtn, fileLabel);
        fileBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button submit = new Button("提交"); submit.getStyleClass().add("btn-primary"); submit.setMaxWidth(Double.MAX_VALUE);
        submit.setOnAction(e -> {
            try {
                String json = String.format("{\"assignment\":{\"id\":%s},\"student\":{\"id\":%d},\"content\":\"%s\",\"submitTime\":\"%s\",\"status\":\"已提交\"}",
                        assignmentId, personId, contentArea2.getText().replace("\"", "\\\"").replace("\n", "\\n"),
                        java.time.LocalDate.now().toString());
                api.post("/api/assignment-submission/add", json);
                s.close();
                new Alert(Alert.AlertType.INFORMATION, "作业提交成功！").show();
            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, "提交失败: " + ex.getMessage()).show(); }
        });
        form.getChildren().addAll(l, contentArea2, fileBar, submit);
        s.setScene(new Scene(form, 500, 400));
        s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        s.show();
    }

    private void showMySubmissions(String assignmentId) {
        Stage s = new Stage(); s.setTitle("我的提交 - 作业ID:" + assignmentId);
        VBox box = new VBox(10); box.setPadding(new Insets(16));
        TableView<Map<String, String>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().add(table);

        new Thread(() -> {
            try {
                String resp = api.get("/api/assignment-submission/my-list?studentId=" + personId);
                List<Map<String, Object>> allSubs = parseList(resp);
                Platform.runLater(() -> {
                    table.getColumns().clear(); table.getItems().clear();
                    // 筛选属于该作业的提交
                    List<Map<String, Object>> filtered = new ArrayList<>();
                    for (Map<String, Object> sub : allSubs) {
                        Object assObj = sub.get("assignment");
                        if (assObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            String aid = String.valueOf(((Map<String,Object>)assObj).get("id"));
                            if (assignmentId.equals(aid)) filtered.add(sub);
                        }
                    }
                    if (filtered.isEmpty()) { table.setPlaceholder(new Label("你还没有提交该作业")); return; }
                    Map<String, Object> f = filtered.get(0);
                    for (String k : f.keySet()) {
                        if ("assignment".equals(k) || "student".equals(k)) continue;
                        TableColumn<Map<String, String>, String> c = new TableColumn<>(k);
                        c.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                                String.valueOf(d.getValue().getOrDefault(k, ""))));
                        table.getColumns().add(c);
                    }
                    for (Map<String, Object> r : filtered) {
                        Map<String, String> rs = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : r.entrySet())
                            rs.put(e.getKey(), e.getValue() instanceof Map ? "(关联)" : String.valueOf(e.getValue()));
                        table.getItems().add(rs);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> table.setPlaceholder(new Label("失败: " + ex.toString())));
            }
        }).start();

        s.setScene(new Scene(box, 600, 400));
        s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        s.show();
    }

    // ========== 统计 ==========

    @FXML
    public void showStatistics() {
        contentArea.getChildren().clear();
        Label title = new Label("📊 我的数据统计");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-padding:0 0 12 0;");
        contentArea.getChildren().add(title);

        FlowPane cards = new FlowPane(16, 16);
        new Thread(() -> {
            try {
                String resp = api.getWithBody("/api/statistics/my-dashboard", personId.toString());
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

    // ========== 退出 ==========

    @FXML
    public void handleLogout() {
        try {
            api.post("/api/auth/logout", "{}");
            api.clearSession();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 520));
            stage.setTitle("登录");
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ========== 辅助 ==========

    private int parseCount(String json) throws Exception {
        Object parsed = api.parse(json);
        if (parsed instanceof List) return ((List<?>) parsed).size();
        if (parsed instanceof Map) {
            // single object (like Person, PreEnrollmentInfo) — count as 1 if has id
            return ((Map<?,?>) parsed).containsKey("id") ? 1 : 0;
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseList(String json) throws Exception {
        Object parsed = api.parse(json);
        if (parsed instanceof List) return (List<Map<String, Object>>) parsed;
        if (parsed instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parsed;
            if (map.containsKey("id")) return List.of(map);
            for (Object v : map.values()) {
                if (v instanceof List) return (List<Map<String, Object>>) v;
            }
        }
        return List.of();
    }

    private String[] getFields(String key) {
        return switch (key) {
            case "person" -> new String[]{"姓名","性别","电话","邮箱","地址","生日"};
            case "family" -> new String[]{"成员姓名","关系","电话","工作单位","职业"};
            case "pre-enrollment" -> new String[]{"高中","省份","城市","毕业年份","高考分数","专业","录取类型"};
            case "social-relation" -> new String[]{"姓名","关系","电话","工作单位","职务","备注"};
            case "honor" -> new String[]{"荣誉名称","级别","获奖时间","描述"};
            case "competition" -> new String[]{"竞赛名称","级别","获奖情况","获奖时间","主办单位","描述"};
            case "science-achievement" -> new String[]{"成果名称","类型","发表时间","发表机构","描述"};
            case "access" -> new String[]{"开始时间","结束时间","事由"};
            case "internship" -> new String[]{"实习单位","实习岗位","开始时间","结束时间","实习地点","描述"};
            case "social-practice" -> new String[]{"实践名称","地点","开始时间","结束时间","担任角色","描述"};
            case "innovation-project" -> new String[]{"项目名称","级别","参与角色","开始时间","结束时间","项目状态","描述"};
            case "training-lecture" -> new String[]{"培训名称","主办方","时间","地点","描述"};
            case "course" -> new String[]{"课程名称","课程编号","学分","课时","教室","上课时间","学期","描述"};
            case "course-selection" -> new String[]{"课程","状态","选课时间"};
            case "attendance" -> new String[]{"课程","考勤日期","状态","备注"};
            case "assignment" -> new String[]{"标题","描述","截止日期","总分"};
            case "daily-activity" -> new String[]{"活动名称","类型","活动时间","地点","角色","时长","描述"};
            case "daily-log" -> new String[]{"标题","类型","日期","内容","心情","标签"};
            default -> new String[]{"名称","描述"};
        };
    }

    private String toJsonKey(String chinese) {
        return switch (chinese) {
            case "姓名"->"name"; case "性别"->"gender"; case "电话"->"phone"; case "邮箱"->"email";
            case "地址"->"address"; case "生日"->"birthday";
            case "成员姓名"->"memberName"; case "关系"->"relation"; case "工作单位"->"workplace"; case "职业"->"occupation";
            case "高中"->"highSchool"; case "省份"->"highSchoolProvince"; case "城市"->"highSchoolCity";
            case "毕业年份"->"graduationYear"; case "高考分数"->"gaokaoScore"; case "专业"->"major"; case "录取类型"->"admissionType";
            case "职务"->"position"; case "备注"->"remark";
            case "荣誉名称"->"name"; case "级别"->"level"; case "获奖时间"->"awardDate";
            case "竞赛名称"->"competitionName"; case "获奖情况"->"award"; case "主办单位"->"organizer";
            case "成果名称"->"achievementName"; case "类型"->"type"; case "发表时间"->"publishDate"; case "发表机构"->"publisher";
            case "开始时间"->"startTime"; case "结束时间"->"endTime"; case "事由"->"reason";
            case "实习单位"->"companyName"; case "实习岗位"->"position"; case "实习地点"->"location";
            case "实践名称"->"practiceName"; case "地点"->"location"; case "担任角色"->"role";
            case "项目名称"->"projectName"; case "参与角色"->"role"; case "项目状态"->"projectStatus";
            case "培训名称"->"trainingName"; case "主办方"->"organizer"; case "时间"->"lectureDate";
            case "课程名称"->"courseName"; case "课程编号"->"courseCode"; case "学分"->"credit"; case "课时"->"hours";
            case "教室"->"classroom"; case "上课时间"->"courseTime"; case "学期"->"semester";
            case "课程"->"course"; case "选课时间"->"selectTime";
            case "考勤日期"->"attendanceDate"; case "状态"->"status";
            case "标题"->"title"; case "截止日期"->"deadline"; case "总分"->"totalScore";
            case "活动名称"->"activityName"; case "活动时间"->"activityDate"; case "角色"->"role"; case "时长"->"duration";
            case "日期"->"logDate"; case "内容"->"content"; case "心情"->"mood"; case "标签"->"tags";
            case "描述"->"description";
            default -> chinese;
        };
    }

    private String getCardStyle(String key) {
        return switch (key) {
            case "person"->"compete"; case "family"->"science"; case "pre-enrollment"->"intern";
            case "social-relation"->"innovate"; case "honor"->"social"; case "competition"->"compete";
            case "science-achievement"->"science"; case "access"->"train";
            case "internship"->"intern"; case "social-practice"->"social";
            case "innovation-project"->"innovate"; case "training-lecture"->"train";
            case "course"->"compete"; case "course-selection"->"science";
            case "attendance"->"activity"; case "assignment"->"log";
            case "daily-activity"->"activity"; case "daily-log"->"log";
            default -> "compete";
        };
    }

    // ========== 模块配置类 ==========

    private static class ModuleDef {
        final String key, name, apiPath, listSuffix, paramStyle, paramName;
        final boolean hasApproval;

        ModuleDef(String key, String name, String apiPath, String listSuffix,
                  String paramStyle, String paramName, boolean hasApproval) {
            this.key = key; this.name = name; this.apiPath = apiPath;
            this.listSuffix = listSuffix; this.paramStyle = paramStyle;
            this.paramName = paramName; this.hasApproval = hasApproval;
        }
    }
}
