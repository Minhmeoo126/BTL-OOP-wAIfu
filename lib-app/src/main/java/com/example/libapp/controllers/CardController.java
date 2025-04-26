package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class CardController {
    @FXML
    public Label bookName;
    @FXML
    public Label AuthorName;
    @FXML
    public ImageView bookImage;
    @FXML
    public HBox box;
    public Button image;
    private Book selectedBook ;

    public void setData(Book newBook) {
        this.selectedBook = newBook;
        try {
            Image image;
            if (newBook.getThumbnail() != null && !newBook.getThumbnail().isEmpty()) {
                image = new Image(newBook.getThumbnail(), true); // true để load nền tránh trườn hợp ảnh nặng
            } else {
                System.out.println("Thumbnail rỗng cho sách: " + newBook.getTitle());
                image = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            }

            bookImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh: " + e.getMessage());
            Image fallback = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            bookImage.setImage(fallback);
        }

        bookName.setText(newBook.getTitle());
        AuthorName.setText(newBook.getAuthorName());
    }


    public void goToBookView() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/book-view.fxml"));
            Parent root = loader.load();

            BookInformationController controller = loader.getController();
            controller.setBook(selectedBook);

            Stage stage = (Stage) image.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
