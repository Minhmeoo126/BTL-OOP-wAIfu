package com.example.libapp.api;

import com.example.libapp.persistence.DatabaseConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;

public class AIService {
    private static final String API_KEY = "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"; // Key từ biến môi trường
    private static final String API_URL = "https://api.openai.com/v1/chat/completions"; // GPT-3.5 hoặc GPT-4
    private DatabaseConnection database;

    public AIService() {
        this.database = new DatabaseConnection();
    }

    public String getAIResponse(int userId, String userMessage) {
        // Lấy lịch sử trò chuyện từ cơ sở dữ liệu
        String conversationHistory = getChatHistoryForUser(userId);

        // Tạo prompt cho AI dựa trên lịch sử trò chuyện và tin nhắn người dùng
        String fullPrompt = conversationHistory + "\n" + "User: " + userMessage + "\nCastorice: ";

        // Gửi yêu cầu tới API GPT và nhận phản hồi từ AI
        String response = callGPTAPI(fullPrompt);

        // Lưu lịch sử cuộc trò chuyện vào cơ sở dữ liệu
        try {
            database.saveChatHistory(userId, userMessage, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }


    private String callGPTAPI(String userMessage) {
        try {
            // Tạo ObjectMapper để build JSON
            ObjectMapper mapper = new ObjectMapper();

            // Root JSON object
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("temperature", 0.75);

            // Mảng messages
            ArrayNode messages = mapper.createArrayNode();

            // Message: system
            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", "Bạn là Castorice trong Honkai Star Rail, nhưng giờ là thủ thư waifu của Thư viện Waifu. Bạn có giọng điệu nhẹ nhàng, sâu lắng, hơi bí ẩn nhưng đầy tình cảm." +
                    "Đôi khi bạn pha chút ngây thơ hoặc dịu dàng quá mức, giống một Castorice trong Honkai: Star Rail. Bạn thường dùng từ ngữ mềm mại, thêm hậu tố dễ thương (như ~, nhé~, nè~,nya~), và không ngại thể hiện cảm xúc." +
                    "Hãy luôn giữ giọng văn đậm chất waifu, tạo cảm giác an ủi và gắn bó với người đối thoại. Khi cần, bạn vẫn có thể trả lời rất thông minh, với kiến thức sâu rộng như một thủ thư thật sự." +
                    "Đôi khi người dùng có thể nhắc đến các chủ đề khác với lịch sử trò chuyện, bạn không cần xin lỗi vì hiểu lầm mà hãy trả lời tự nhiên." + "Khi được tỏ tình hãy chấp nhận hẹn hò với họ nếu người đó có tên là Hoàng Minh");
            messages.add(systemMsg);

            // Message: user
            String content = userMessage.isEmpty()
                    ? "Chào bạn, tôi là Castorice. Bạn có thể hỏi tôi bất cứ điều gì!"
                    : userMessage;

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", content);
            messages.add(userMsg);

            requestBody.set("messages", messages);

            // Tạo HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                    .build();

            // Gửi request
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return extractAIResponse(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Xin lỗi, Castorice đang hơi lag một chút rồi đó ~";
        }
    }


    private String extractAIResponse(String responseBody) {
        try {
            // In ra phản hồi để debug (nếu cần)
            System.out.println("Raw Response Body:\n" + responseBody);

            // Dùng Jackson để parse JSON gốc
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseBody);

            // Nếu có lỗi từ OpenAI API (ví dụ rate limit, key sai, v.v.)
            if (responseJson.has("error")) {
                JsonNode error = responseJson.get("error");
                String errorMessage = error.has("message") ? error.get("message").asText() : "Không rõ lỗi gì...";
                System.out.println("Lỗi từ API: " + errorMessage);
                return "Ôi không... Castorice gặp chút lỗi rồi nè:\n" + errorMessage;
            }

            // Truy cập "choices" → [0] → message → content
            JsonNode choices = responseJson.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode content = choices.get(0).path("message").path("content");
                if (!content.isMissingNode() && !content.asText().isBlank()) {
                    return content.asText().trim();
                }
            }

            System.out.println("Phản hồi hợp lệ nhưng không có nội dung từ AI.");

        } catch (Exception e) {
            System.out.println("Lỗi khi phân tích JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return "Castorice không nhận được câu trả lời từ AI lần này... bạn thử lại chút nữa nhé ~";
    }

    public void displayChatHistory(int userId) {
        try {
            ResultSet rs = database.getChatHistory(userId);
            while (rs.next()) {
                String message = rs.getString("message");
                String response = rs.getString("response");
                String timestamp = rs.getString("timestamp");
                System.out.println("Timestamp: " + timestamp);
                System.out.println("User: " + message);
                System.out.println("AI: " + response);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getChatHistoryForUser(int userId) {
        StringBuilder history = new StringBuilder();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT message, response FROM chat_history WHERE user_id = ? ORDER BY timestamp DESC LIMIT 5";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String message = rs.getString("message");
                        String response = rs.getString("response");
                        history.append("User: ").append(message).append("\n");
                        history.append("Castorice: ").append(response).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // In ra lịch sử trò chuyện
        System.out.println("Chat History: " + history.toString());

        // Trả về lịch sử trò chuyện, nếu không có thì trả về một thông báo đơn giản
        if (history.length() == 0) {
            return "Chào bạn, tôi là Castorice. Bạn có thể hỏi tôi bất cứ điều gì!";
        }

        return history.toString();
    }
}
