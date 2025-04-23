package com.example.libapp;

import com.example.libapp.api.BookService;
import com.example.libapp.persistence.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database and fetch books in a background thread
        new Thread(() -> {
            try {
                // Test database connection
                try (Connection conn = DatabaseConnection.connect()) {
                    System.out.println("Database connected successfully!");
                    System.out.println("Database setup completed!");
                } catch (SQLException e) {
                    System.err.println("Database connection error:");
                    e.printStackTrace();
                    return; // Exit the thread if connection fails
                }

                // Initialize the books table
                BookService.initializeDatabase();

                // Check the number of books in the database
                int bookCount = BookService.countBooks();
                if (bookCount >= 3000) {
                    System.out.println("Database already contains " + bookCount + " books. Skipping fetch to save API quota.");
                    return;
                }

                // Define diverse queries for various genres, including comics and Vietnamese books
                List<String> queries = Arrays.asList(
                        "Java Programming", // Programming
                        "Fiction",          // Fiction
                        "Science Fiction",  // Sci-Fi
                        "Fantasy",          // Fantasy
                        "Mystery",          // Mystery
                        "Romance",          // Romance
                        "History",          // History
                        "Biography",        // Biography
                        "Comics",           // Comics
                        "Graphic Novels",   // Graphic Novels
                        "inlanguage:vi"     // Vietnamese books
                );
                // Thêm thể loại vào đây để lấy thêm sách.

                // Fetch books for each query, aiming for ~10,000 books
                for (String query : queries) {
                    BookService.fetchAndStoreBooks(query, 1000); // Limit per query to balance diversity
                    System.out.println("Books fetched for query: " + query);
                }
                System.out.println("Books fetched and stored successfully!");
            } catch (Exception e) {
                System.err.println("Error during database setup or API fetch:");
                e.printStackTrace();
            }
        }).start();

        // Load JavaFX UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/libapp/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
        stage.setTitle("Library Management App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}