package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BorrowingHistoryViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final ObservableList<BorrowingRecord> records = FXCollections.observableArrayList();
    private User loggedInUser;

    public ObservableList<BorrowingRecord> getRecords() {
        return records;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void loadHistory() {
        if (loggedInUser == null) {
            records.clear();
            return;
        }
        records.setAll(borrowingRecordDAO.getAllBorrowingRecords().stream()
                .filter(record -> record.getUserId() == loggedInUser.getId())
                .collect(java.util.stream.Collectors.toList()));
    }
}