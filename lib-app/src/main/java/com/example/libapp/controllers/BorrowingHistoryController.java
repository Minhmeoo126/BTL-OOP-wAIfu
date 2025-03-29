package com.example.libapp.controllers;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class BorrowingHistoryController {
    @FXML
    private TableView<BorrowingRecord> historyTable;
    @FXML
    private TableColumn<BorrowingRecord, Integer> bookIdColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowingRecord, String> returnDateColumn;

    private BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        List<BorrowingRecord> userRecords = borrowingRecordDAO.getAllBorrowingRecords().stream()
                .filter(record -> record.getUserId() == LoginController.getLoggedInUser().getId())
                .collect(Collectors.toList());
        historyTable.getItems().addAll(userRecords);
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }
}