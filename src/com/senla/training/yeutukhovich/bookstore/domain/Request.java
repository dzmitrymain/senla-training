package com.senla.training.yeutukhovich.bookstore.domain;

public class Request extends AbstractEntity {

    private static final long serialVersionUID = 2837429729065366794L;

    private Book book;
    private Boolean isActive;
    private String requesterData;

    public Request() {

    }

    public Request(Book book, String requesterData) {
        this.requesterData = requesterData;
        this.book = book;
        isActive = true;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
