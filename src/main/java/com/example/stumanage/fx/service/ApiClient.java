package com.example.stumanage.fx.service;

import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * API 客户端 — JavaFX 通过 HTTP 请求和 Spring Boot 后端通信
 * 后端地址：http://localhost:22222
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:22222";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    private String sessionCookie;  // 登录成功后保存的 Session ID

    /**
     * 发送 GET 请求
     */
    public String get(String path) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();
        if (sessionCookie != null) {
            builder.header("Cookie", sessionCookie);
        }
        HttpResponse<String> resp = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());
        // 保存服务端返回的 Session Cookie
        resp.headers().firstValue("Set-Cookie").ifPresent(c -> sessionCookie = c.split(";")[0]);
        return resp.body();
    }

    /**
     * 发送 GET 请求（带请求体，用于 /my-list 这类接口）
     */
    public String getWithBody(String path, String body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .method("GET", HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json");
        if (sessionCookie != null) {
            builder.header("Cookie", sessionCookie);
        }
        HttpResponse<String> resp = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());
        resp.headers().firstValue("Set-Cookie").ifPresent(c -> sessionCookie = c.split(";")[0]);
        return resp.body();
    }

    /**
     * 发送 POST 请求
     */
    public String post(String path, String jsonBody) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");
        if (sessionCookie != null) {
            builder.header("Cookie", sessionCookie);
        }
        HttpResponse<String> resp = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());
        resp.headers().firstValue("Set-Cookie").ifPresent(c -> sessionCookie = c.split(";")[0]);
        return resp.body();
    }

    /**
     * 发送 PUT 请求
     */
    public String put(String path, String jsonBody) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");
        if (sessionCookie != null) {
            builder.header("Cookie", sessionCookie);
        }
        HttpResponse<String> resp = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());
        return resp.body();
    }

    /**
     * 发送 DELETE 请求
     */
    public void delete(String path) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE();
        if (sessionCookie != null) {
            builder.header("Cookie", sessionCookie);
        }
        client.send(builder.build(), HttpResponse.BodyHandlers.discarding());
    }

    /** 解析 JSON 字符串（自动判断是对象还是数组） */
    public Object parse(String json) throws Exception {
        if (json.trim().startsWith("[")) {
            return mapper.readValue(json, List.class);
        }
        return mapper.readValue(json, Map.class);
    }

    /** 解析 JSON 为 Map */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseMap(String json) throws Exception {
        Object result = parse(json);
        if (result instanceof List) {
            // 数组包成 Map 返回，兼容老代码
            return Map.of("list", result);
        }
        return (Map<String, Object>) result;
    }

    /** 对象转 JSON 字符串 */
    public String toJson(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    /** Session 是否有效 */
    public boolean isLoggedIn() {
        return sessionCookie != null;
    }

    /** 清除登录状态 */
    public void clearSession() {
        sessionCookie = null;
    }
}
