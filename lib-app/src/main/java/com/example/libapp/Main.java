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

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database and fetch books in a background thread
        new Thread(() -> {
            try {
                // Test database connection (replacing DatabaseSetup logic)
                try (Connection conn = DatabaseConnection.connect()) {
                    System.out.println("Database connected successfully!");
                    System.out.println("Database setup completed!");
                } catch (SQLException e) {
                    System.err.println("Database connection error:");
                    e.printStackTrace();
                    return; // Exit the thread if connection fails
                }

                // Initialize the books table (if not already created by DatabaseConnection)
                BookService.initializeDatabase();

                // Fetch and store books from Google Books API
                BookService.fetchAndStoreBooks("Java Programming"); // Example query
                System.out.println("Books fetched and stored successfully!");
            } catch (Exception e) {
                System.err.println("Error during database setup or API fetch:");
                e.printStackTrace();
            }
        }).start();

        // Load JavaFX UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/libapp/view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Library Management App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}