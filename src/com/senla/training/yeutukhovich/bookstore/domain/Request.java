package com.senla.training.yeutukhovich.bookstore.domain;

public class Request extends AbstractEntity {

    private static int requestIdNumber;

    private Book book;
    private boolean isActive;
    private String requesterData;

    public Request(Book book, String requesterData) {
        super(++requestIdNumber);
        this.requesterData = requesterData;
        this.book = book;
        isActive = true;
    }

    public Book getBook() {
        return book;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRequesterData() {
        return requesterData;
    }

    public void setRequesterData(String requesterData) {
        this.requesterData = requesterData;
    }

    @Override
    public Request clone() {
        Request newRequest = (Request) super.clone();
        newRequest.book = this.book.clone();
        return newRequest;
    }

    @Override
    public String toString() {
        return "Request{" +
                "book=" + book +
                ", isActive=" + isActive +
                ", requesterData='" + requesterData + '\'' +
                ", id=" + id +
                '}';
    }
}
