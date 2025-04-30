package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.api.BookService;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.AuthorAndCategoryInDatabase;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class addBookController {
    @FXML
    public Button AI,backToMain,myAccount,addBook,bookManagement,userManagement;
    @FXML
    public Button logout,chooseImageButton;
    @FXML
    public Label UserName,messageLabel;
    @FXML
    public ImageView image;
    @FXML
    public TextField category,isbnField,thumbnail,bookName,AuthorName;
    @FXML
    public TextArea description;

    private final BookDAO bookDAO = new BookDAO();
    private final MainViewModel viewModel = new MainViewModel();

    public void initialize() {
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

                AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(fetchedBook.getAuthorName(),messageLabel,fetchedBook);
                AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(fetchedBook.getCategoryName(),messageLabel,fetchedBook);

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
        Image newimage;

        if (newBookThumbnail.isEmpty()) {
            newBookThumbnail = "";
            newimage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
        } else {
            try {
                newimage = new Image(newBookThumbnail, true);
                if (newimage.isError()) throw new IllegalArgumentException();
            } catch (Exception e) {
                newimage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            }
        }
        image.setImage(newimage);
        newBook.setThumbnail(newBookThumbnail);

        AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(newBookAuthor,messageLabel,newBook);
        AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(newBookCategory,messageLabel,newBook);

        newBook.setTotalCopies(5);
        newBook.setAvailableCopies(5);

        bookDAO.addBook(newBook);
        messageLabel.setText("Thêm sách tự xuất bản thành công.");
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void chooseImageFromFileSystem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Đường dẫn đích: lib-app/self_published/
                File destDir = new File("self_published");
                if (!destDir.exists()) destDir.mkdirs();

                // Tên file giữ nguyên
                File destFile = new File(destDir, selectedFile.getName());

                // Sao chép file
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Preview ảnh
                Image newImage = new Image(destFile.toURI().toString());
                image.setImage(newImage);

                // Lưu đường dẫn tương đối
                thumbnail.setText(selectedFile.getName());

            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Không thể tải ảnh");
            }
        }
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

            AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(fetchedBook.getAuthorName(),messageLabel,fetchedBook);
            AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(fetchedBook.getCategoryName(), messageLabel,fetchedBook);

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

}

