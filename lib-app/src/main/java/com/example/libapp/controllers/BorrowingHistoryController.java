package com.example.libapp.controllers;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.BorrowingHistoryViewModel;
import com.example.libapp.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class BorrowingHistoryController {
    @FXML
    private Button backToMain;
    @FXML
    private TableView<BorrowingRecord> historyTable;
    @FXML
    private TableColumn<BorrowingRecord, Integer> bookIdColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> returnDateColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> authorNameColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> bookNameColumn;

    private final BorrowingHistoryViewModel viewModel = new BorrowingHistoryViewModel();

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        historyTable.setItems(viewModel.getRecords());
        viewModel.setLoggedInUser(SessionManager.getInstance().getLoggedInUser());
        viewModel.loadHistory();
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }
}
