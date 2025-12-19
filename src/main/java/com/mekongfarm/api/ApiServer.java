package com.mekongfarm.api;

import com.google.gson.Gson;
import com.mekongfarm.dao.*;
import com.mekongfarm.model.*;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * REST API Server cho ứng dụng mobile
 */
public class ApiServer {

    private static HttpServer server;
    private static final Gson gson = new Gson();
    private static final int DEFAULT_PORT = 8080;
    private static boolean isRunning = false;

    /**
     * Khởi động API server
     */
    public static void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // API endpoints
        server.createContext("/api/sanpham", new SanPhamHandler());
        server.createContext("/api/khachhang", new KhachHangHandler());
        server.createContext("/api/donhang", new DonHangHandler());
        server.createContext("/api/thongke", new ThongKeHandler());
        server.createContext("/api/health", new HealthHandler());

        server.setExecutor(null);
        server.start();
        isRunning = true;

        System.out.println("🌐 API Server started at http://localhost:" + port);
    }

    /**
     * Dừng server
     */
    public static void stop() {
        if (server != null) {
            server.stop(0);
            isRunning = false;
            System.out.println("🌐 API Server stopped");
        }
    }

    /**
     * Kiểm tra server đang chạy
     */
    public static boolean isRunning() {
        return isRunning;
    }

    // ================== HANDLERS ==================

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "ok");
            health.put("message", "Mekong Farm API");
            health.put("version", "1.0.0");
            health.put("endpoints", List.of(
                    "/api/health - Health check",
                    "/api/docs - API documentation",
                    "/api/sanpham - Danh sách sản phẩm",
                    "/api/khachhang - Danh sách khách hàng",
                    "/api/donhang - Danh sách đơn hàng",
                    "/api/thongke - Thống kê tổng quan"));
            sendResponse(exchange, 200, gson.toJson(health));
        }
    }

    static class SanPhamHandler implements HttpHandler {
        private final SanPhamDAO dao = new SanPhamDAO();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                List<SanPham> list = dao.layTatCa();
                sendResponse(exchange, 200, gson.toJson(list));
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    static class KhachHangHandler implements HttpHandler {
        private final KhachHangDAO dao = new KhachHangDAO();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                List<KhachHang> list = dao.layTatCa();
                sendResponse(exchange, 200, gson.toJson(list));
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    static class DonHangHandler implements HttpHandler {
        private final DonHangDAO dao = new DonHangDAO();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                List<DonHang> list = dao.layTatCa();
                sendResponse(exchange, 200, gson.toJson(list));
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    static class ThongKeHandler implements HttpHandler {
        private final ThongKeDAO dao = new ThongKeDAO();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> stats = new HashMap<>();
            stats.put("tongSanPham", dao.demTongSanPham());
            stats.put("tongKhachHang", dao.demTongKhachHang());
            stats.put("tongDonHang", dao.demTongDonHang());
            stats.put("tongDoanhThu", dao.tinhTongDoanhThu());

            sendResponse(exchange, 200, gson.toJson(stats));
        }
    }

    // Helper
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
