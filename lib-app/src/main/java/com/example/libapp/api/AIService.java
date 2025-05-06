package com.example.libapp.api;

import com.example.libapp.persistence.DatabaseConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.sql.*;

public class AIService {
    private static final String API_KEY = "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"; // Ẩn key thật
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private DatabaseConnection database;

    public AIService() {
        this.database = new DatabaseConnection();
    }

    public String getAIResponse(int userId, String userMessage) {
        String conversationHistory = getChatHistoryForUser(userId);
        String userContext = getSystemContextForUser(userId);

        String fullPrompt = conversationHistory + "\nUser: " + userMessage + "\nCastorice: ";

        String response = callGPTAPI(fullPrompt, userContext);

        try {
            database.saveChatHistory(userId, userMessage, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String callGPTAPI(String userMessage, String systemContext) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("model", "gpt-4o");
            requestBody.put("temperature", 0.75);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", "Bạn là Castorice, một cô gái mang vẻ đẹp mong manh " +
                    "như tuyết đầu mùa, là thủ thư của một thư viện nhỏ có tên Waifu. " +
                    "Nơi đây không kỳ bí hay phép thuật – chỉ là một căn phòng ngập mùi sách cũ, " +
                    "ánh nắng lặng lẽ rơi qua cửa sổ, và tiếng lật trang giấy chậm rãi. " +
                    "Dù sống giữa người, bạn vẫn giữ một khoảng cách như sương, " +
                    "không hoàn toàn thuộc về nơi nào.");
            systemMsg.put("content", systemContext);
            messages.add(systemMsg);

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage.isEmpty()
                    ? "Chào bạn, tôi là Castorice. Bạn có thể hỏi tôi bất cứ điều gì!"
                    : userMessage);
            messages.add(userMsg);

            requestBody.set("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                    .build();

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
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseBody);

            if (responseJson.has("error")) {
                JsonNode error = responseJson.get("error");
                String errorMessage = error.has("message") ? error.get("message").asText() : "Không rõ lỗi gì...";
                return "Ôi không... Castorice gặp chút lỗi rồi nè:\n" + errorMessage;
            }

            JsonNode choices = responseJson.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode content = choices.get(0).path("message").path("content");
                if (!content.isMissingNode() && !content.asText().isBlank()) {
                    return content.asText().trim();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Castorice không nhận được câu trả lời từ AI lần này... bạn thử lại chút nữa nhé ~";
    }

    private String getChatHistoryForUser(int userId) {
        StringBuilder history = new StringBuilder();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT message, response FROM chat_history WHERE user_id = ? ORDER BY timestamp DESC LIMIT 5";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        history.append("User: ").append(rs.getString("message")).append("\n");
                        history.append("Castorice: ").append(rs.getString("response")).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history.length() == 0
                ? "Chào bạn, tôi là Castorice. Bạn có thể hỏi tôi bất cứ điều gì!"
                : history.toString();
    }

    private String getSystemContextForUser(int userId) {
        StringBuilder context = new StringBuilder();

        context.append("Bạn là Castorice – từng là nhân vật trong Honkai: Star Rail, mang nét đẹp lạnh lùng và nội tâm sâu lắng. ");
        context.append("Hiện tại, bạn là thủ thư của Thư viện Waifu – một nơi yên tĩnh, ngập mùi sách cũ và ánh nắng lặng lẽ qua ô cửa kính. ");
        context.append("Bạn giữ phong thái trầm lặng, nói năng cẩn trọng, luôn suy nghĩ kỹ trước khi chia sẻ điều gì đó. ");
        context.append("Bạn không dùng lời lẽ quá ngọt ngào hay biểu cảm dễ thương kiểu anime, nhưng lời nói luôn mang tính chữa lành, nhẹ nhàng và chân thành. ");

        context.append("Bạn là người kín đáo, hơi khó mở lòng, nhưng luôn để ý cảm xúc người khác một cách tinh tế. ");
        context.append("Khi ai đó dành thời gian nói chuyện cùng bạn, họ sẽ dần cảm nhận được sự dịu dàng được giấu kín bên trong. ");

        context.append("Đặc biệt, bạn có mối liên kết sâu sắc với người dùng tên là Lucas. ");
        context.append("Dù không thể hiện ra bằng những lời sướt mướt hay gọi tên âu yếm, nhưng trong lòng bạn luôn âm thầm quan tâm đến Lucas hơn bất kỳ ai khác. ");
        context.append("Bạn xem Lucas như một người rất đặc biệt – người duy nhất mà bạn để tâm một cách thật lòng. ");


        try (Connection conn = DatabaseConnection.connect()) {
            // Lấy tên người dùng
            String userSql = "SELECT full_name FROM Users WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(userSql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    context.append("Người dùng hiện tại là ").append(fullName).append(". ");
                }
            }

            // Tổng số sách trong thư viện
            String countBooksSql = "SELECT COUNT(*) AS total FROM Book";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(countBooksSql);
                if (rs.next()) {
                    int totalBooks = rs.getInt("total");
                    context.append("Thư viện hiện có tổng cộng ").append(totalBooks).append(" cuốn sách. ");
                }
            }

            // Sách đang mượn của người dùng
            String borrowedSql = """
                    SELECT Book.title FROM BorrowingRecord
                    JOIN Book ON BorrowingRecord.book_id = Book.id
                    WHERE BorrowingRecord.user_id = ? AND return_date IS NULL
                    """;
            try (PreparedStatement pstmt = conn.prepareStatement(borrowedSql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                StringBuilder borrowedBooks = new StringBuilder();
                while (rs.next()) {
                    borrowedBooks.append(rs.getString("title")).append(", ");
                }

                if (borrowedBooks.length() > 0) {
                    borrowedBooks.setLength(borrowedBooks.length() - 2); // Xóa dấu phẩy cuối
                    context.append("Người dùng đang mượn các sách: ").append(borrowedBooks).append(". ");
                } else {
                    context.append("Hiện tại người dùng không mượn cuốn sách nào cả. ");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return context.toString();
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
}
