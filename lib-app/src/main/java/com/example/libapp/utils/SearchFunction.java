package com.example.libapp.utils;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class SearchFunction {

    public static ObservableList<Book> searchFunction(ObservableList<Book> booklist, String keyword) {
        // Dam bao so sach tuong duong vs table view
        ObservableList<Book> filteredBooklist = FXCollections.observableArrayList();
        if (keyword == null || keyword.trim().isEmpty()) {
            return filteredBooklist;//tra ve toan bo sach
        } else {
            String filter = keyword.toLowerCase();
            for (Book book : booklist) {
                if (book.getTitle().toLowerCase().startsWith(filter)
                        || String.valueOf(book.getId()).startsWith(filter)
                        || book.getAuthorName().toLowerCase().startsWith(filter)) {
                    filteredBooklist.add(book);
                }
            }
        }
        return filteredBooklist;
    }

    public static ObservableList<Book> searchTitleFunction(ObservableList<Book> booklist, String keyword) {
        // Dam bao so sach tuong duong vs table view
        ObservableList<Book> filteredBooklist = FXCollections.observableArrayList();
        if (keyword == null || keyword.trim().isEmpty()) {
            return filteredBooklist;//tra ve toan bo sach
        } else {
            String filter = keyword.toLowerCase();
            for (Book book : booklist) {
                if (book.getTitle().toLowerCase().startsWith(filter)) {
                    filteredBooklist.add(book);
                }
            }
        }
        return filteredBooklist;
    }

    public static ObservableList<Book> searchIDFunction(ObservableList<Book> booklist, String keyword) {
        // Dam bao so sach tuong duong vs table view
        ObservableList<Book> filteredBooklist = FXCollections.observableArrayList();
        if (keyword == null || keyword.trim().isEmpty()) {
            return filteredBooklist;//tra ve toan bo sach
        } else {
            String filter = keyword.toLowerCase();
            for (Book book : booklist) {
                if (String.valueOf(book.getId()).startsWith(filter)) {
                    filteredBooklist.add(book);
                }
            }
        }
        return filteredBooklist;
    }

    public static ObservableList<Book> searchAuthorFunction(ObservableList<Book> booklist, String keyword) {
        // Dam bao so sach tuong duong vs table view
        ObservableList<Book> filteredBooklist = FXCollections.observableArrayList();
        if (keyword == null || keyword.trim().isEmpty()) {
            return filteredBooklist;//tra ve toan bo sach
        } else {
            String filter = keyword.toLowerCase();
            for (Book book : booklist) {
                if (book.getAuthorName().toLowerCase().startsWith(filter)) {
                    filteredBooklist.add(book);
                }
            }
        }
        return filteredBooklist;
    }

    public static void Search(TextField search , GridPane Box , Pane searchResultBox) throws IOException {
        BookDAO bookDAO = new BookDAO();
        List<Book> allBooks = bookDAO.getAllBooks();
        String keyWord = search.getText();
        Box.getChildren().clear();
        ObservableList<Book> searchBook = SearchFunction.searchFunction(FXCollections.observableArrayList(allBooks), keyWord);

        if (searchBook.isEmpty()) {
            Label noResultLabel = new Label("Không tìm thấy sách nào");
            noResultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            Box.add(noResultLabel, 0, 0);
            // Thay đổi: Hiển thị searchResultBox mà không gọi toFront()
            searchResultBox.setVisible(true);
            return;
        }

        BookGridPane.makeGridPaneForHBox(searchBook, 0, Math.min(10, searchBook.size()), Box, 1);

        if (searchBook.size() > 10) {
            Button viewAllButton = new Button("Xem tất cả");
            viewAllButton.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

            viewAllButton.setOnAction(e -> {
                try {
                    showAllBooks(searchBook , search);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Box.add(viewAllButton, 0, 11);
        }

        // Thay đổi: Hiển thị searchResultBox mà không gọi toFront()
        searchResultBox.setVisible(true);
    }

    private static void showAllBooks(ObservableList<Book> searchBook, TextField search) throws IOException {
        SceneNavigator.loadSearchResult(search);
    }
}
