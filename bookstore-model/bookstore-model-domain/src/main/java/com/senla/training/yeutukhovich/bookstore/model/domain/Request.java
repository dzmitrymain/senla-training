package com.senla.training.yeutukhovich.bookstore.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requests")
public class Request extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "requester_data")
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
                ", is active=" + isActive +
                ", requester data='" + requesterData +
                "']";
    }
}
