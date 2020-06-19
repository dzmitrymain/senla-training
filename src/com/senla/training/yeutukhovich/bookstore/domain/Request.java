package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

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
    public String toString() {
        return "Request [id=" + id +
                ", book title=" + book.getTitle() +
                ", is active=" + isActive +
                ", requester data='" + requesterData +
                "']";
    }
}
