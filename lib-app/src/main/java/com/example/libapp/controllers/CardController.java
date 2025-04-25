package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    public HBox box;

    public void setData(Book newBook) {
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
}
