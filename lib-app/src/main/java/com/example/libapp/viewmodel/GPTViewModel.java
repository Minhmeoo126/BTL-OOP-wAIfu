package com.example.libapp.viewmodel;

import com.example.libapp.api.GPTClient;
import com.example.libapp.model.BookSuggestion;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPTViewModel {
    private final StringProperty prompt = new SimpleStringProperty();
    private final StringProperty response = new SimpleStringProperty();
    private final ObservableList<BookSuggestion> bookSuggestions = FXCollections.observableArrayList();
    private final GPTClient gptClient;

    public GPTViewModel() {
        this.gptClient = new GPTClient();
    }

    public StringProperty promptProperty() {
        return prompt;
    }

    public StringProperty responseProperty() {
        return response;
    }

    public ObservableList<BookSuggestion> getBookSuggestions() {
        return bookSuggestions;
    }

    public void getGPTResponse() {
        String promptText = prompt.get();
        if (promptText != null && !promptText.isEmpty()) {
            String gptResponse = gptClient.getGPTResponse(promptText);
            response.set(gptResponse);
            parseBookSuggestions(gptResponse);
        } else {
            response.set("Please enter a question!");
        }
    }

    private void parseBookSuggestions(String gptResponse) {
        bookSuggestions.clear();
        // Pattern to match book suggestions in the format: "Book Title" by Author
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s+by\\s+([^\\n]+)");
        Matcher matcher = pattern.matcher(gptResponse);

        while (matcher.find()) {
            String title = matcher.group(1);
            String author = matcher.group(2).trim();
            bookSuggestions.add(new BookSuggestion(title, author));
        }
    }
}