package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class SearchResultController {
    @FXML
    public GridPane bookContainer1, bookContainer2, bookContainer3;
    @FXML
    public Pagination pagination1, pagination2, pagination3;
    @FXML
    public Button backToMain;

    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();
    public static final int BOOKS_PER_PAGE = 4;
    private String keyword;

    public void initialize() {
    }

    public void Search() throws IOException {
        ObservableList<Book> searchTitleBook = SearchFunction.searchForEachFunction(FXCollections.observableArrayList(allBooks), keyword, "Title");

        ObservableList<Book> searchIDBook = SearchFunction.searchForEachFunction(FXCollections.observableArrayList(allBooks), keyword,"ID");

        ObservableList<Book> searchAuthorBook = SearchFunction.searchForEachFunction(FXCollections.observableArrayList(allBooks), keyword,"Author");

        SearchForEach(searchTitleBook, bookContainer1, pagination1);
        SearchForEach(searchIDBook, bookContainer2, pagination2);
        SearchForEach(searchAuthorBook, bookContainer3, pagination3);
    }

    private void SearchForEach(ObservableList<Book> searchBook, GridPane bookContainer, Pagination pagination) {
        bookContainer.getChildren().clear(); // xóa nội dung cũ
        pagination.setPageFactory(null);
        pagination.setPageCount(1);

        if (searchBook.isEmpty()) {
            Label noResultLabel = new Label("No Result!!");
            noResultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            bookContainer.add(noResultLabel, 0, 0);
            pagination.setVisible(false);
        } else {
            pagination.setVisible(true);
            int totalPages = (int) Math.ceil(searchBook.size() / (double) BOOKS_PER_PAGE);
            pagination.setPageCount(totalPages);

            pagination.setPageFactory(pageIndex -> createSearchPage(searchBook, pageIndex));
        }
    }

    private GridPane createSearchPage(List<Book> books, int pageIndex) {
        GridPane gridPane = new GridPane();
        int start = pageIndex * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, books.size());
        BookGridPane.makeGridPaneForVBox(books, start, end, gridPane, 2);
        return gridPane;
    }


    // lấy keyword từ main
    public void setSearchKeyword(String keyword) throws IOException {
        this.keyword = keyword;
        Search();
    }
    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }
}
