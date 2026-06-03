package com.example.stumanage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;

/**
 * 前端入口：启动 JavaFX 桌面客户端（自动启动后端）
 *
 * 运行方式：在 IDEA 右键此类 → Run 'JavaFxLauncher'
 *         或用命令行：.\mvnw javafx:run
 */
public class JavaFxLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 520);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("学生综合管理系统 - 山东大学软件学院");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        // 先在新线程启动 Spring Boot 后端，再启动 JavaFX 前端
        new Thread(() -> SpringApplication.run(StuManageApplication.class, args)).start();
        // 稍等一下让后端先起来
        try { Thread.sleep(3000); } catch (InterruptedException e) { }
        launch(args);
    }
}
