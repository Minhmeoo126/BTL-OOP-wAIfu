package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.controllers.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class BorrowingHistoryViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final ObservableList<BorrowingRecord> history = FXCollections.observableArrayList();

    public BorrowingHistoryViewModel() {
        int userId = LoginController.getLoggedInUser().getId();
        history.addAll(borrowingRecordDAO.getAllBorrowingRecords().stream()
                .filter(record -> record.getUserId() == userId)
                .collect(Collectors.toList()));
    }

    public ObservableList<BorrowingRecord> getHistory() {
        return history;
    }
}