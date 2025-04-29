package com.example.libapp.utils;

import com.example.libapp.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
}
