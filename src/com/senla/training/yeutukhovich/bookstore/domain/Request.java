package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.util.Objects;

public class Request extends AbstractEntity {

    private Book book;
    private Boolean isActive;
    private String requesterData;

    public Request(Book book, String requesterData) {
        super(IdGenerator.getInstance().getNextRequestIdNumber());
        this.requesterData = requesterData;
        this.book = book;
        isActive = true;
    }

    public Book getBook() {
        return book;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getRequesterData() {
        return requesterData;
    }

    public void setRequesterData(String requesterData) {
        this.requesterData = requesterData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        if (!Objects.equals(book, request.book)) {
            return false;
        }
        if (!Objects.equals(isActive, request.isActive)) {
            return false;
        }
        return Objects.equals(requesterData, request.requesterData);
    }

    @Override
    public int hashCode() {
        int result = book != null ? book.hashCode() : 0;
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (requesterData != null ? requesterData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Request [id=" + id +
                ", book title=" + book.getTitle() +
                ", is active=" + isActive +
                ", requester data='" + requesterData +
                "']";
    }
}
