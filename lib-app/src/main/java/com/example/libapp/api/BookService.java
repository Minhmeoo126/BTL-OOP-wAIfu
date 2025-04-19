package com.example.libapp.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.charset.StandardCharsets;

public class BookService {
    private static final String API_KEY = "AIzaSyBzoGmsExfyPpiqoVOAjkvxzp8R1V-Wb2o"; // Your Google Books API key
    private static final String DB_URL = "jdbc:sqlite:lib-app/lib.db"; // Align with DatabaseConnection

    public static void initializeDatabase() throws SQLException {
        // Schema updates are handled by DatabaseConnection
    }

    public static void fetchAndStoreBooks(String query) throws Exception {
        // Encode the query parameter to handle spaces and special characters
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY;
        String jsonResponse = fetchBooksFromApi(apiUrl);
        parseAndStoreBooks(jsonResponse);
    }

    private static String fetchBooksFromApi(String apiUrl) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static void parseAndStoreBooks(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        JsonNode items = root.path("items");

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Ensure a default category exists
            int categoryId = getOrCreateCategory(conn, "Programming");

            // Insert books
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO Book (title, author_id, category_id, total_copies, available_copies, description) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.path("volumeInfo");

                    String title = volumeInfo.path("title").asText("");
                    if (title.isEmpty()) continue; // Skip books with missing titles

                    String author = "";
                    JsonNode authorsNode = volumeInfo.path("authors");
                    if (authorsNode.isArray() && authorsNode.size() > 0) {
                        String[] authors = mapper.treeToValue(authorsNode, String[].class);
                        author = authors[0]; // Take the first author
                    }
                    if (author.isEmpty()) author = "Unknown Author";

                    // Get the description (introduction/preview)
                    String description = volumeInfo.path("description").asText(null); // Null if not present

                    // Insert or get author_id
                    int authorId = getOrCreateAuthor(conn, author);

                    // Set default values for copies
                    int totalCopies = 1;
                    int availableCopies = 1;

                    // Insert into Book table
                    pstmt.setString(1, title);
                    pstmt.setInt(2, authorId);
                    pstmt.setInt(3, categoryId);
                    pstmt.setInt(4, totalCopies);
                    pstmt.setInt(5, availableCopies);
                    pstmt.setString(6, description); // Can be null
                    pstmt.executeUpdate();
                }
            }
        }
    }

    private static int getOrCreateAuthor(Connection conn, String authorName) throws SQLException {
        // Check if author exists
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM Author WHERE name = ?")) {
            pstmt.setString(1, authorName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Insert new author
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Author (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert author: " + authorName);
    }

    private static int getOrCreateCategory(Connection conn, String categoryName) throws SQLException {
        // Check if category exists
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM Category WHERE name = ?")) {
            pstmt.setString(1, categoryName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Insert new category
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Category (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert category: " + categoryName);
    }

    // Method to retrieve book details including description for display
    public static BookDetails getBookDetails(int bookId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT b.title, a.name AS author, c.name AS category, b.description " +
                             "FROM Book b " +
                             "JOIN Author a ON b.author_id = a.id " +
                             "JOIN Category c ON b.category_id = c.id " +
                             "WHERE b.id = ?")) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new BookDetails(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("description")
                );
            }
        }
        return null; // Book not found
    }

    // Inner class to hold book details
    public static class BookDetails {
        private final String title;
        private final String author;
        private final String category;
        private final String description;

        public BookDetails(String title, String author, String category, String description) {
            this.title = title;
            this.author = author;
            this.category = category;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }
    }
}