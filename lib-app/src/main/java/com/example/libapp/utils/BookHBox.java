package com.example.libapp.utils;

import com.example.libapp.controllers.CardController;
import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// nguồn tham khảo https://www.youtube.com/@mahmoudhamwi4550(JavaFX UI: Library Design & Dynamic HBox and GridPane)
public class BookHBox {

    private static final BookDAO bookDAO = new BookDAO();

    private static List<Book> recentlyAdded() {
        List<Book> recentlyAdded = new ArrayList<>();
        List<Book> a = new ArrayList<>(bookDAO.getAllBooks());
        for (int i = a.size() - 1; i >= a.size() - 10; i--) {
            recentlyAdded.add(a.get(i));
        }
        return recentlyAdded;
    }

    public static void setUpRecentlyAddBook(HBox cardLayout) {
        List<Book> recentlyAdd = new ArrayList<>(recentlyAdded());
        try {
            for (Book newBook : recentlyAdd) {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
                HBox cardBook = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(newBook);
                cardLayout.getChildren().add(cardBook);
            }

        } catch (
                IOException e) {
            System.err.println("Lỗi khi load Bookcard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
