package com.mekongfarm.test;

import com.mekongfarm.service.GeminiService;

public class TestGeminiAI {
    public static void main(String[] args) {
        System.out.println("=== TEST GEMINI AI ===\n");
        
        GeminiService geminiService = new GeminiService();
        
        // Test 1: Chatbot
        System.out.println("📝 Test 1: Chatbot - Hỏi về kỹ thuật trồng lúa");
        String response1 = geminiService.chat("Cho tôi 3 mẹo trồng lúa ST25 hiệu quả?");
        System.out.println("Phản hồi: " + response1);
        System.out.println("\n" + "=".repeat(80) + "\n");
        
        // Test 2: Phân tích dữ liệu
        System.out.println("📊 Test 2: Phân tích dữ liệu kinh doanh");
        String sampleData = """
                Doanh thu tháng 1: 50,000,000 VNĐ
                Doanh thu tháng 2: 65,000,000 VNĐ
                Doanh thu tháng 3: 45,000,000 VNĐ
                Sản phẩm bán chạy: Gạo ST25, Xoài cát Hòa Lộc
                """;
        String response2 = geminiService.analyzeData(sampleData);
        System.out.println("Phản hồi: " + response2);
        System.out.println("\n=== TEST HOÀN TẤT ===");
    }
}
