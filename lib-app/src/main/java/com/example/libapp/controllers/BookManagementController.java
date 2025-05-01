package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.BookViewModel;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class BookManagementController {
    @FXML public Button AI;
    @FXML public Button myAccount;
    @FXML public Button addBook;
    @FXML public Button bookManage;
    @FXML public Button userManagement;
    public Button logout;
    public Label UserName;
    @FXML private Button backToMain;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book,Integer> IDColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, Integer> totalCopiesColumn;
    @FXML private TableColumn<Book, Integer> availableCopiesColumn;
    @FXML private TextField searchBookField;

    private final BookViewModel viewModel = new BookViewModel();

    @FXML
    public void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        titleColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(titleColumn.widthProperty().subtract(10));
                setGraphic(text);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        authorColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(authorColumn.widthProperty().subtract(10));
                setGraphic(text);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        categoryColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(categoryColumn.widthProperty().subtract(10));
                setGraphic(text);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        FilteredList<Book> filteredBooks = new FilteredList<>(viewModel.getBooks(), b -> true);

        searchBookField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredBooks.setPredicate(book -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return (book.getTitle() != null && book.getTitle().toLowerCase().contains(lower)) ||
                        (book.getAuthorName() != null && book.getAuthorName().toLowerCase().contains(lower)) ||
                        (book.getCategoryName() != null && book.getCategoryName().toLowerCase().contains(lower)) ||
                        (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lower));
            });
        });

        bookTable.setItems(filteredBooks);

        viewModel.loadBooks();

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        UserName.setText(currentUser != null ? currentUser.getUsername() : "khong co nguoi dung");
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount(ActionEvent event) throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    public void addNewBook(ActionEvent event) throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManage();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManage();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void goToAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    @FXML
    public void deleteSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this book?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection conn = DatabaseConnection.connect()) {
                        String sql = "DELETE FROM Book WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, selectedBook.getId());
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    updateBookList();

                    showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Book deleted successfully!");
                }
            });

        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select book to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateBookList() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.getBooks().remove(selectedBook);
        }
    }
}
