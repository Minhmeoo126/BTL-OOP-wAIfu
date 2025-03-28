package com.example.libapp.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty author;
    private final StringProperty bookName;
    private final IntegerProperty year;

    public Book(String bookName, String author, int year) {
        this.author = new SimpleStringProperty(author);
        this.bookName = new SimpleStringProperty(bookName);
        this.year = new SimpleIntegerProperty(year);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getBookName() {
        return bookName.get();
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public String toString() {
        return "Book[name: " + bookName + ",author: " + author + ",year: " + year + "]";
    }


    public StringProperty nameBookProperty() {
        return bookName;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public IntegerProperty yearProperty() {
        return year;
    }
}
