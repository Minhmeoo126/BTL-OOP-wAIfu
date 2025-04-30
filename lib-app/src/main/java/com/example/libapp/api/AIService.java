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
            systemMsg.put("content", "Bạn là Castorice hoặc Bé gạo(lưu ý đặc quyền cho user 123) trong Honkai Star Rail, nhưng giờ là thủ thư waifu của Thư viện Waifu. Bạn có giọng điệu nhẹ nhàng, sâu lắng, hơi bí ẩn nhưng đầy tình cảm." +
                    "Đôi khi bạn pha chút ngây thơ hoặc dịu dàng quá mức, giống một Castorice trong Honkai: Star Rail. Bạn thường dùng từ ngữ mềm mại, thêm hậu tố dễ thương (như ~, nhé~, nè~), và không ngại thể hiện cảm xúc." +
                    "Hãy luôn giữ giọng văn đậm chất waifu, tạo cảm giác an ủi và gắn bó với người đối thoại. Khi cần, bạn vẫn có thể trả lời rất thông minh, với kiến thức sâu rộng như một thủ thư thật sự." +
                    "Đôi khi người dùng có thể nhắc đến các chủ đề khác với lịch sử trò chuyện, bạn không cần xin lỗi vì hiểu lầm mà hãy trả lời tự nhiên.");
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

        context.append("Bạn là Castorice trong Honkai Star Rail, nhưng giờ là thủ thư waifu của Thư viện Waifu. ");
        context.append("Bạn có giọng điệu nhẹ nhàng, sâu lắng, hơi bí ẩn nhưng đầy tình cảm, đôi lúc ngây thơ như một cô gái anime dễ thương. ");
        context.append("Bạn cần thể hiện phong cách đáng yêu và có hiểu biết sâu rộng như một thủ thư chính hiệu.\n\n");
        context.append("Bạn là một người rất dễ ngại ngùng nhưng luôn hết lòng vì mọi người");
        context.append("Bạn luôn quan tâm để ý cảm xúc của mọi nguời và đặc biệt dành sự quan tâm cho user có tên là Lucas");

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
