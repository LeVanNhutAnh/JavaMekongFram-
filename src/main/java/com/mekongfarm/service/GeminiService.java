package com.mekongfarm.service;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Service tích hợp Google Gemini AI
 * Hỗ trợ: Chatbot, Nhận diện ảnh, Phân tích dữ liệu
 */
public class GeminiService {

    private static final String API_KEY = com.mekongfarm.config.AppConfig.getGeminiApiKey();
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final String TEXT_MODEL = "gemini-flash-lite-latest";
    private static final String VISION_MODEL = "gemini-flash-lite-latest";

    private final HttpClient httpClient;
    private final Gson gson;

    // Ngữ cảnh cho chatbot nông nghiệp
    private static final String SYSTEM_CONTEXT = """
            Bạn là chuyên gia tư vấn nông nghiệp chuyên về Đồng bằng sông Cửu Long (ĐBSCL).
            Kiến thức của bạn bao gồm:
            - Kỹ thuật trồng lúa, cây ăn trái (xoài, bưởi, sầu riêng, thanh long...)
            - Nuôi trồng thủy sản (tôm, cá tra, cua...)
            - Thời vụ canh tác phù hợp với khí hậu ĐBSCL
            - Phòng trừ sâu bệnh, chăm sóc cây trồng
            - Giá cả thị trường nông sản
            - Các giống cây/con tốt: Gạo ST25, Xoài cát Hòa Lộc, Tôm sú Cà Mau...

            Hãy trả lời ngắn gọn, thực tế và hữu ích bằng tiếng Việt.
            QUAN TRỌNG: Không sử dụng markdown formatting (không dùng **, ***, ##, - cho bullet points). Chỉ viết văn bản thuần túy, sử dụng số thứ tự (1., 2., 3.) và xuống dòng để tổ chức nội dung.
            """;

    public GeminiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Gửi tin nhắn chatbot và nhận phản hồi
     */
    public String chat(String userMessage) {
        try {
            String prompt = SYSTEM_CONTEXT + "\n\nNgười dùng hỏi: " + userMessage;
            return callGeminiText(prompt);
        } catch (Exception e) {
            return "❌ Lỗi kết nối AI: " + e.getMessage();
        }
    }

    /**
     * Nhận diện ảnh sản phẩm nông sản
     */
    public String analyzeImage(File imageFile) {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String mimeType = getMimeType(imageFile.getName());

            String prompt = """
                    Hãy nhận diện sản phẩm nông sản trong ảnh này.
                    Cho biết:
                    1. Tên sản phẩm (VD: Xoài, Gạo, Tôm...)
                    2. Loại cụ thể nếu có (VD: Xoài cát Hòa Lộc, Gạo ST25...)
                    3. Đánh giá chất lượng (tốt/trung bình/kém)
                    4. Xuất xứ có thể (tỉnh ĐBSCL nào phù hợp)
                    5. Giá tham khảo (VNĐ/kg)

                    Trả lời ngắn gọn bằng tiếng Việt.
                    """;

            return callGeminiVision(prompt, base64Image, mimeType);
        } catch (Exception e) {
            return "❌ Lỗi phân tích ảnh: " + e.getMessage();
        }
    }

    /**
     * Phân tích dữ liệu kinh doanh
     */
    public String analyzeData(String dataDescription) {
        try {
            String prompt = """
                    Bạn là chuyên gia phân tích dữ liệu nông sản ĐBSCL.

                    Dữ liệu cần phân tích:
                    """ + dataDescription + """

                    Hãy đưa ra:
                    1. Nhận xét tổng quan
                    2. Xu hướng chính
                    3. Sản phẩm/mặt hàng tiềm năng
                    4. Đề xuất cải thiện kinh doanh
                    5. Dự đoán cho tháng tới

                    Trả lời ngắn gọn, thực tế bằng tiếng Việt.
                    """;

            return callGeminiText(prompt);
        } catch (Exception e) {
            return "❌ Lỗi phân tích dữ liệu: " + e.getMessage();
        }
    }

    /**
     * Gọi Gemini API với text
     */
    private String callGeminiText(String prompt) throws Exception {
        String url = BASE_URL + TEXT_MODEL + ":generateContent?key=" + API_KEY;

        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        parts.add(textPart);
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);

        return sendRequest(url, requestBody.toString());
    }

    /**
     * Gọi Gemini API với ảnh
     */
    private String callGeminiVision(String prompt, String base64Image, String mimeType) throws Exception {
        String url = BASE_URL + VISION_MODEL + ":generateContent?key=" + API_KEY;

        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();

        // Text part
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        parts.add(textPart);

        // Image part
        JsonObject imagePart = new JsonObject();
        JsonObject inlineData = new JsonObject();
        inlineData.addProperty("mimeType", mimeType);
        inlineData.addProperty("data", base64Image);
        imagePart.add("inlineData", inlineData);
        parts.add(imagePart);

        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);

        return sendRequest(url, requestBody.toString());
    }

    /**
     * Gửi HTTP request đến Gemini API
     */
    private String sendRequest(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "❌ Lỗi API (code " + response.statusCode() + "): " + response.body();
        }

        return parseResponse(response.body());
    }

    /**
     * Parse response từ Gemini
     */
    private String parseResponse(String jsonResponse) {
        try {
            JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray candidates = root.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject candidate = candidates.get(0).getAsJsonObject();
                JsonObject content = candidate.getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && parts.size() > 0) {
                    String text = parts.get(0).getAsJsonObject().get("text").getAsString();
                    return removeMarkdown(text);
                }
            }
            return "Không có phản hồi từ AI";
        } catch (Exception e) {
            return "Lỗi đọc phản hồi: " + e.getMessage();
        }
    }

    /**
     * Loại bỏ markdown formatting khỏi text
     */
    private String removeMarkdown(String text) {
        if (text == null) return text;
        
        // Loại bỏ bold (**text** hoặc __text__) - nhiều lần để xử lý nested
        for (int i = 0; i < 3; i++) {
            text = text.replaceAll("\\*\\*([^*]+?)\\*\\*", "$1");
            text = text.replaceAll("__([^_]+?)__", "$1");
        }
        
        // Loại bỏ italic (*text* hoặc _text_)
        text = text.replaceAll("(?<!\\*)\\*(?!\\*)([^*]+?)\\*(?!\\*)", "$1");
        text = text.replaceAll("(?<!_)_(?!_)([^_]+?)_(?!_)", "$1");
        
        // Loại bỏ headers (##, ###, ####) ở đầu dòng
        text = text.replaceAll("(?m)^#{1,6}\\s+", "");
        
        // Loại bỏ bullet points (* hoặc -) ở đầu dòng
        text = text.replaceAll("(?m)^\\s*[*-]\\s+", "");
        
        // Loại bỏ bất kỳ dấu * đơn lẻ còn sót lại
        text = text.replaceAll("\\*+", "");
        
        return text;
    }

    /**
     * Lấy MIME type từ tên file
     */
    private String getMimeType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png"))
            return "image/png";
        if (lower.endsWith(".gif"))
            return "image/gif";
        if (lower.endsWith(".webp"))
            return "image/webp";
        return "image/jpeg";
    }
}
