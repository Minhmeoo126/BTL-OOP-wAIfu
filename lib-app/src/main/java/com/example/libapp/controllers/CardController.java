package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class CardController {
    @FXML
    public Label bookName;
    @FXML
    public Label AuthorName;
    @FXML
    public ImageView bookImage;
    @FXML
    public VBox box;

    public void setData(Book newBook){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newBook.getThumbnail())));
        bookImage.setImage(image);
        bookName.setText(newBook.getTitle());
        AuthorName.setText(newBook.getAuthorName());
    }
}
