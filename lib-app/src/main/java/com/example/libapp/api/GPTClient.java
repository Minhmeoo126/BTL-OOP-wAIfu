/*package com.example.libapp.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPTClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String sendRequest(String input) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            // Tạo JSON request
            String jsonInput = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}]}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] inputBytes = jsonInput.getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            // Nhận response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }
            conn.disconnect();

            // Parse JSON để lấy nội dung trả lời
            String responseString = response.toString();
            int contentStart = responseString.indexOf("\"content\":\"") + 11;
            int contentEnd = responseString.indexOf("\"", contentStart);
            if (contentStart > 10 && contentEnd > contentStart) {
                return responseString.substring(contentStart, contentEnd).replace("\\n", "\n");
            } else {
                return "Không thể phân tích phản hồi từ API.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi kết nối API: " + e.getMessage();
        }
    }
}

 */