package com.example.libapp.utils;

import com.example.libapp.controllers.BookController;
import com.example.libapp.controllers.CardController;
import com.example.libapp.model.Book;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class BookGridPane {

    public static void makeGridPaneForVBox(List<Book> allBooks, int start, int end, GridPane bookGridPane , int maxCol) {
        int col = 0;
        int row = 1;

        try {
            for (int i = start; i < end; i++) {
                Book book = allBooks.get(i);

                FXMLLoader loader = new FXMLLoader(BookGridPane.class.getResource("/com/example/libapp/view/Gridpane_bookcard-view.fxml"));
                VBox bookBox = loader.load();

                BookController bookController = loader.getController();
                bookController.setData(book);

                if (col == maxCol) {
                    col = 0;
                    ++row;
                }

                bookGridPane.add(bookBox, col++, row);

                GridPane.setMargin(bookBox, new Insets(6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeGridPaneForHBox(List<Book> allBooks,int start,int end ,GridPane gridPane , int maxCol){
        int col = 0;
        int row = 1;

        try {
            for (int i = start; i < end; i++) {
                Book book = allBooks.get(i);

                FXMLLoader loader = new FXMLLoader(BookGridPane.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
                HBox bookBox = loader.load();

                CardController cardController = loader.getController();
                cardController.setData(book);

                if (col == maxCol) {
                    col = 0;
                    ++row;
                }

                gridPane.add(bookBox, col++, row);

                GridPane.setMargin(bookBox, new Insets(6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

