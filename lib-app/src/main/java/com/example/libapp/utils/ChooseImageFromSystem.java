package com.example.libapp.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ChooseImageFromSystem {
    public static void chooseImage(Button chooseImageButton , ImageView image , TextField thumbnail , Label messageLabel) {
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
}
