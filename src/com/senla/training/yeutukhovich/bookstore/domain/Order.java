package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;

import java.math.BigDecimal;
import java.util.Date;

public class Order extends AbstractEntity {

    private OrderState state;
    private Book book;
    private BigDecimal currentBookPrice;
    private Date creationDate;
    private Date completionDate;
    private String customerData;

    public Order() {

    }

    public Order(Book book, String customerData) {
        state = OrderState.CREATED;
        this.book = book;
        currentBookPrice = book.getPrice();
        creationDate = new Date();
        this.customerData = customerData;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigDecimal getCurrentBookPrice() {
        return currentBookPrice;
    }

    public void setCurrentBookPrice(BigDecimal currentBookPrice) {
        this.currentBookPrice = currentBookPrice;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getCustomerData() {
        return customerData;
    }

    public void setCustomerData(String customerData) {
        this.customerData = customerData;
    }

    @Override
    public String toString() {
        return "Order [id=" + id +
                ", state=" + state +
                ", customer data='" + customerData +
                "', book title='" + book.getTitle() + "']";
    }
}
