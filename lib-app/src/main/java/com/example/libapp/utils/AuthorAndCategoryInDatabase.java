package com.example.libapp.utils;

import com.example.libapp.model.Author;
import com.example.libapp.model.Book;
import com.example.libapp.model.Category;
import com.example.libapp.persistence.AuthorDAO;
import com.example.libapp.persistence.CategoryDAO;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class AuthorAndCategoryInDatabase {
    private static final AuthorDAO authorDAO = new AuthorDAO();
    private static final CategoryDAO categoryDAO = new CategoryDAO();

    public static void checkAndAddIfAuthorNotInDataBase(String newBookAuthor, Label messageLabel, Book newBook) {
        Integer authorId = null;
        try {
            // Kiểm tra xem tác giả đã tồn tại trong cơ sở dữ liệu chưa
            authorId = authorDAO.getAuthorIdByName(newBookAuthor);
            if (authorId != null) {
                System.out.println("Tác giả đã tồn tại với ID: " + authorId);
            }
        } catch (SQLException e) {
            messageLabel.setText("Không thể xác định tác giả.");
            e.printStackTrace();
            return;
        }

        if (authorId == null) {
            Author newAuthor = new Author();
            newAuthor.setName(newBookAuthor);
            newAuthor.setBio("");
            try {
                authorDAO.addAuthor(newAuthor);
                // Lấy lại ID của tác giả sau khi thêm
                authorId = authorDAO.getAuthorIdByName(newBookAuthor);
                System.out.println("Tác giả mới đã được thêm với ID: " + authorId);
            } catch (SQLException e) {
                messageLabel.setText("Lỗi khi thêm tác giả.");
                e.printStackTrace();
                return;
            }
        }
        if (authorId == null) {
            messageLabel.setText("Không thể xác định tác giả.");
            return;
        }
        newBook.setAuthorId(authorId);
    }

    public static void checkAndAddIfCategoryNotInDataBase(String category, Label messageLabel, Book newBook) {
        Integer categoryID = null;
        try {
            // Kiểm tra xem tác giả đã tồn tại trong cơ sở dữ liệu chưa
            categoryID = categoryDAO.getCategoryIdByName(category);
            if (categoryID != null) {
                System.out.println("Category đã tồn tại với ID: " + categoryID);
            }
        } catch (SQLException e) {
            messageLabel.setText("Không thể xác định Category.");
            e.printStackTrace();
            return;
        }

        if (categoryID == null) {
            Category category1 = new Category();
            category1.setName(category);
            try {
                categoryDAO.addCategory(category1);
                categoryID = categoryDAO.getCategoryIdByName(category);
                System.out.println("Category mới đã được thêm với ID: " + categoryID);
            } catch (SQLException e) {
                messageLabel.setText("Lỗi khi thêm category.");
                e.printStackTrace();
                return;
            }
        }
        if (categoryID == null) {
            messageLabel.setText("Không thể xác định category.");
            return;
        }
        newBook.setCategoryId(categoryID);
    }

    public static int checkAndAddCategory(String category, Label messageLabel) {
        Integer categoryID = null;
        try {
            // Kiểm tra xem tác giả đã tồn tại trong cơ sở dữ liệu chưa
            categoryID = categoryDAO.getCategoryIdByName(category);
            if (categoryID != null) {
                System.out.println("Category đã tồn tại với ID: " + categoryID);
            }
        } catch (SQLException e) {
            messageLabel.setText("Không thể xác định Category.");
            e.printStackTrace();
        }

        if (categoryID == null) {
            Category category1 = new Category();
            category1.setName(category);
            try {
                categoryDAO.addCategory(category1);
                categoryID = categoryDAO.getCategoryIdByName(category);
                System.out.println("Category mới đã được thêm với ID: " + categoryID);
            } catch (SQLException e) {
                messageLabel.setText("Lỗi khi thêm category.");
                e.printStackTrace();
            }
        }
        if (categoryID == null) {
            messageLabel.setText("Không thể xác định category.");

        }
        return categoryID;
    }

    public static int checkAndAddAuthor(String newBookAuthor, Label messageLabel) {
        Integer authorId = null;
        try {
            // Kiểm tra xem tác giả đã tồn tại trong cơ sở dữ liệu chưa
            authorId = authorDAO.getAuthorIdByName(newBookAuthor);
            if (authorId != null) {
                System.out.println("Tác giả đã tồn tại với ID: " + authorId);
            }
        } catch (SQLException e) {
            messageLabel.setText("Không thể xác định tác giả.");
            e.printStackTrace();
        }

        if (authorId == null) {
            Author newAuthor = new Author();
            newAuthor.setName(newBookAuthor);
            newAuthor.setBio("");
            try {
                authorDAO.addAuthor(newAuthor);
                // Lấy lại ID của tác giả sau khi thêm
                authorId = authorDAO.getAuthorIdByName(newBookAuthor);
                System.out.println("Tác giả mới đã được thêm với ID: " + authorId);
            } catch (SQLException e) {
                messageLabel.setText("Lỗi khi thêm tác giả.");
                e.printStackTrace();
            }
        }
        if (authorId == null) {
            messageLabel.setText("Không thể xác định tác giả.");
        }
        return authorId;
    }
}
