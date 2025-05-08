package com.example.libapp.utils;

import com.example.libapp.model.Book;
import javafx.scene.image.Image;

import java.io.File;

public class LoadImage {
    public static Image loadImage(Book book) {

        try {
            Image image = null;
            if (book.getThumbnail() != null && !book.getThumbnail().isEmpty()) {
                if (book.getThumbnail().startsWith("http://") || book.getThumbnail().startsWith("https://")) {
                    // Load từ URL
                    image = new Image(book.getThumbnail(), true);
                } else {
                    // Giả sử là đường dẫn tương đối tới thư mục self_published
                    File imageFile = new File("self_published", book.getThumbnail()); // tự động nối thư mục và tên file
                    if (imageFile.exists()) {
                        image = new Image(imageFile.toURI().toString());
                    } else {
                        System.out.println("Không tìm thấy ảnh trong thư mục self_published: " + imageFile.getPath());
                    }
                }
            }
            // Nếu không load được ảnh thì dùng mặc định
            if (image == null || image.isError()) {
                System.out.println("Không thể load ảnh, dùng ảnh mặc định.");
                image = new Image(LoadImage.class.getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            }

            return image;

        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh: " + e.getMessage());
            return new Image(LoadImage.class.getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
        }
    }

}
