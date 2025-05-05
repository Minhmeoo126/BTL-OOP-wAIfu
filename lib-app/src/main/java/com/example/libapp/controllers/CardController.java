package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.utils.LoadImage;
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

import java.io.File;
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
    @FXML
    public Button image;
    @FXML
    private Book selectedBook ;

    public void setData(Book newBook) {
        this.selectedBook = newBook;
        bookImage.setImage(LoadImage.loadImage(newBook));
        bookName.setText(newBook.getTitle());
        AuthorName.setText(newBook.getAuthorName());
    }


    public void goToBookView() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/book-view.fxml"));
            Parent root = loader.load();

            BookInformationController controller = loader.getController();
            controller.loadBookData(selectedBook);

            Stage stage = (Stage) image.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
