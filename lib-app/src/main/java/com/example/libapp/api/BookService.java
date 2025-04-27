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
import java.util.HashSet;
import java.util.Set;

public class BookService {
    private static final String API_KEY = "AIzaSyBzoGmsExfyPpiqoVOAjkvxzp8R1V-Wb2o"; // Your Google Books API key
    private static final String DB_URL = "jdbc:sqlite:lib-app/lib.db"; // Align with DatabaseConnection
    private static final int MAX_RESULTS_PER_CALL = 40; // Google Books API max results per request
    public static void initializeDatabase() throws SQLException {
        // Schema updates are handled by DatabaseConnection
    }

    public static int countBooks() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Book");
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public static void fetchAndStoreBooks(String query, int maxBooks) throws Exception {
        int startIndex = 0;
        int booksFetched = 0;

        while (booksFetched < maxBooks) {
            // Encode the query parameter to handle spaces and special characters
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery +
                    "&maxResults=" + MAX_RESULTS_PER_CALL + "&startIndex=" + startIndex + "&key=" + API_KEY;

            String jsonResponse = fetchBooksFromApi(apiUrl);
            int booksInThisBatch = parseAndStoreBooks(jsonResponse);

            booksFetched += booksInThisBatch;
            startIndex += MAX_RESULTS_PER_CALL;

            // Break if no more books are returned
            if (booksInThisBatch == 0) {
                break;
            }

            // Avoid overwhelming the API
            Thread.sleep(100); // Respect API rate limits
        }
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

    private static int parseAndStoreBooks(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        JsonNode items = root.path("items");

        if (items.isMissingNode() || items.size() == 0) {
            return 0; // No books in this response
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String categoryName = determineCategoryFromQuery(items.path(0).path("volumeInfo").path("categories"));
            int categoryId = getOrCreateCategory(conn, categoryName);

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO Book (id, title, author_id, category_id, total_copies, available_copies, description, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                int booksAdded = 0;

                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.path("volumeInfo");

                    // 👉 Fetch ISBN
                    String isbn = extractIsbn(volumeInfo);
                    if (isbn == null) continue; // Skip nếu không có ISBN

                    // 👉 Skip nếu đã tồn tại trong database
                    if (isBookExists(conn, isbn)) {
                        continue;
                    }

                    String title = volumeInfo.path("title").asText("");
                    if (title.isEmpty()) continue;

                    String author = "";
                    JsonNode authorsNode = volumeInfo.path("authors");
                    if (authorsNode.isArray() && authorsNode.size() > 0) {
                        String[] authors = mapper.treeToValue(authorsNode, String[].class);
                        author = authors[0];
                    }
                    if (author.isEmpty()) author = "Unknown Author";

                    String description = volumeInfo.path("description").asText(null);
                    String thumbnailUrl = volumeInfo.path("imageLinks").path("thumbnail").asText(null);

                    int authorId = getOrCreateAuthor(conn, author);
                    int totalCopies = 1;
                    int availableCopies = 1;

                    pstmt.setString(1, isbn); // 🛠 Set id = ISBN
                    pstmt.setString(2, title);
                    pstmt.setInt(3, authorId);
                    pstmt.setInt(4, categoryId);
                    pstmt.setInt(5, totalCopies);
                    pstmt.setInt(6, availableCopies);
                    pstmt.setString(7, description);
                    pstmt.setString(8, thumbnailUrl);

                    pstmt.executeUpdate();
                    booksAdded++;
                }
                return booksAdded;
            }
        }
    }

    private static String extractIsbn(JsonNode volumeInfo) {
        JsonNode identifiers = volumeInfo.path("industryIdentifiers");
        if (identifiers.isArray()) {
            for (JsonNode identifier : identifiers) {
                String type = identifier.path("type").asText();
                if ("ISBN_13".equals(type) || "ISBN_10".equals(type)) {
                    return identifier.path("identifier").asText();
                }
            }
        }
        return null; // Không có ISBN
    }

    private static boolean isBookExists(Connection conn, String isbn) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM Book WHERE id = ?")) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String determineCategoryFromQuery(JsonNode categoriesNode) {
        if (categoriesNode.isArray() && categoriesNode.size() > 0) {
            return categoriesNode.get(0).asText("General");
        }
        return "General"; // Default category if none specified
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
                     "SELECT b.title, a.name AS author, c.name AS category, b.description, b.thumbnail " +
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
                        rs.getString("description"),
                        rs.getString("thumbnail") // Fetch thumbnail
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
        private final String thumbnail;

        public BookDetails(String title, String author, String category, String description, String thumbnail) {
            this.title = title;
            this.author = author;
            this.category = category;
            this.description = description;
            this.thumbnail = thumbnail;
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

        public String getThumbnail() {
            return thumbnail;
        }
    }


}
