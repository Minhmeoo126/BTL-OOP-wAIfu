package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.api.BookScanner;
import com.example.libapp.api.BookService;
import com.example.libapp.api.ISBNScannerWindow;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.*;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class addBookController {
    @FXML
    public Button AI, backToMain, myAccount, addBook, bookManagement, userManagement;
    @FXML
    public Button logout, chooseImageButton;
    @FXML
    public Label UserName, messageLabel;
    @FXML
    public ImageView image;
    @FXML
    public TextField category, isbnField, thumbnail, bookName, AuthorName,search;
    @FXML
    public TextArea description;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public GridPane Box;
    @FXML
    public StackPane mainPane;

    private final BookDAO bookDAO = new BookDAO();
    private final MainViewModel viewModel = new MainViewModel();

    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        searchResultBox.setVisible(false);

        // Định vị searchResultBox ngay dưới TextField khi focus
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchResultBox.setLayoutX(400);
                searchResultBox.setLayoutY(50);
                searchResultBox.setPrefWidth(415.0);
                searchResultBox.setPrefHeight(150.0);

                searchResultBox.setVisible(true);
                searchResultBox.toFront();
                System.out.println("SearchBar focused: true → searchResultBox visible at X: " + searchResultBox.getLayoutX() + ", Y: " + searchResultBox.getLayoutY());
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    if (!pane.isHover() && !search.isFocused()) {
                        searchResultBox.setVisible(false);
                        System.out.println("SearchBar lost focus + searchResultBox not hovered → searchResultBox hidden");
                    }
                });
                pause.play();
            }
        });

        // Thay đổi: Thêm sự kiện mousePressed cho searchResultBox để cho phép nhấp xuyên qua nó
        searchResultBox.setPickOnBounds(false);

        // Thay đổi: Cập nhật logic mousePressed trên mainPane để đảm bảo BorderPane có thể tương tác
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus();// ép search mất focus
            searchResultBox.setVisible(false);
            searchResultBox.toBack();
            search.setText("");
        });

        // Gọi Search khi nội dung TextField thay đổi
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Search();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
    }

    public void AddNewBook(ActionEvent event) {
        String isbn = isbnField.getText().trim();

        // Nếu có ISBN
        if (!isbn.isEmpty()) {
            Book existing = bookDAO.getBookByISBN(isbn);
            if (existing != null) {
                if (!confirmAction("Sách đã tồn tại. Bạn có muốn cập nhật số lượng bản sao không?")) return;
                existing.setTotalCopies(existing.getTotalCopies() + 1);
                existing.setAvailableCopies(existing.getAvailableCopies() + 1);
                bookDAO.updateBook(existing);
                messageLabel.setText("Đã cập nhật số lượng bản sao.");
                return;
            }

            Book fetchedBook = BookService.fetchBookByIsbn(isbn);
            if (fetchedBook != null) {
                if (!confirmAction("Xác nhận thêm sách \"" + fetchedBook.getTitle() + "\" từ Google Books?")) return;

                fetchedBook.setTotalCopies(5);
                fetchedBook.setAvailableCopies(5);

                AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(fetchedBook.getAuthorName(), messageLabel, fetchedBook);
                AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(fetchedBook.getCategoryName(), messageLabel, fetchedBook);

                bookDAO.addBook(fetchedBook);
                messageLabel.setText("Đã thêm sách từ Google Books API.");
                return;
            }

            messageLabel.setText("Không tìm thấy sách với ISBN này.");
            return;
        }

        // Nếu là sách tự xuất bản (ISBN rỗng)
        String newBookName = bookName.getText().trim();
        String newBookAuthor = AuthorName.getText().trim();
        String newBookDescription = description.getText().trim();
        String newBookCategory = category.getText().trim();

        if (newBookName.isEmpty() || newBookAuthor.isEmpty() || newBookDescription.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin cho sách tự xuất bản.");
            return;
        }

        if (!confirmAction("Xác nhận thêm sách tự xuất bản?")) return;

        Book newBook = new Book();
        newBook.setIsbn(BookService.generateInternalIsbn());
        newBook.setTitle(newBookName);
        newBook.setAuthorName(newBookAuthor);
        newBook.setDescription(newBookDescription);

        String newBookThumbnail = thumbnail.getText().trim();
        newBook.setThumbnail(newBookThumbnail);
        image.setImage(LoadImage.loadImage(newBook));

        AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(newBookAuthor, messageLabel, newBook);
        AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(newBookCategory, messageLabel, newBook);


        Book checkBook = bookDAO.getBookByTitle(newBookName);


        if (checkBook != null) {
            if (checkBook.getThumbnail().equals(newBook.getThumbnail())) {
                if (checkBook.getDescription().equals(newBook.getDescription())
                        && checkBook.getAuthorName().equals(newBook.getAuthorName())
                        && checkBook.getCategoryId() == newBook.getCategoryId()) {

                    System.out.println("Sách đã tồn tại");
                    System.out.println("Số lượng sách hiện tại: " + checkBook.getTotalCopies());
                    if (!confirmAction("Sách đã tồn tại xác nhận thêm số luợng ?")) return;
                    bookDAO.updateBookCopies(checkBook.getId(), checkBook.getTotalCopies() + 1, checkBook.getAvailableCopies() + 1);
                    messageLabel.setText("Đã cập nhật số lượng bản sao sách tự xuất bản.");
                    return;
                }
            }else{
                newBook.setAvailableCopies(5);
                newBook.setTotalCopies(5);
                bookDAO.addBook(newBook);
                messageLabel.setText("Thêm sách tự xuất bản thành công.");
            }
        } else {
            newBook.setAvailableCopies(5);
            newBook.setTotalCopies(5);
            bookDAO.addBook(newBook);
            messageLabel.setText("Thêm sách tự xuất bản thành công.");
        }
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);

    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml", bookManagement);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    public void chooseImageFromFileSystem(ActionEvent event) {
        ChooseImageFromSystem.chooseImage(chooseImageButton,image,thumbnail,messageLabel);
    }

    private boolean handleAddBookByIsbn(String isbn) {
        Book existing = bookDAO.getBookByISBN(isbn);
        if (existing != null) {
            existing.setTotalCopies(existing.getTotalCopies() + 1);
            existing.setAvailableCopies(existing.getAvailableCopies() + 1);
            bookDAO.updateBook(existing);
            messageLabel.setText("Sách đã tồn tại. Đã tăng số lượng bản sao.");
            return true;
        }

        Book fetchedBook = BookService.fetchBookByIsbn(isbn);
        if (fetchedBook != null) {
            // Gán copies mặc định
            fetchedBook.setTotalCopies(5);
            fetchedBook.setAvailableCopies(5);

            AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(fetchedBook.getAuthorName(), messageLabel, fetchedBook);
            AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(fetchedBook.getCategoryName(), messageLabel, fetchedBook);

            bookDAO.addBook(fetchedBook);
            messageLabel.setText("Đã thêm sách từ Google Books API.");
            return true;
        }

        messageLabel.setText("Không tìm thấy sách với ISBN này.");
        return false;
    }


    private boolean confirmAction(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }


    public void Search() throws IOException {
        SearchFunction.Search(search,Box,searchResultBox);
    }

    public void scanAndAddISBN(ActionEvent event) {
        messageLabel.setText("Đang quét ISBN từ webcam...");

        // Tạo thread mới để chạy camera feed và quét ISBN
        Thread cameraThread = new Thread(() -> {
            // Quá trình quét ISBN
            String scannedIsbn = ISBNScannerWindow.launchAndScan();  // Quét ISBN

            // Cập nhật lại giao diện chính khi có kết quả ISBN
            Platform.runLater(() -> {
                if (scannedIsbn != null) {
                    isbnField.setText(scannedIsbn);  // Cập nhật trường ISBN

                    // Xử lý thêm sách vào hệ thống (hoặc cập nhật)
                    boolean success = handleAddBookByIsbn(scannedIsbn);
                    if (success) {
                        messageLabel.setText("📚 Đã thêm sách hoặc cập nhật bản sao thành công.");
                    } else {
                        messageLabel.setText("❌ Không tìm thấy sách với ISBN đã quét.");
                    }
                } else {
                    messageLabel.setText("❌ Không quét được mã ISBN.");
                }
            });
        });

        cameraThread.setDaemon(true);  // Đảm bảo thread tự tắt khi ứng dụng đóng
        cameraThread.start();
    }

}

