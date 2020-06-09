package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;

import java.math.BigDecimal;
import java.util.Date;

public class Order extends AbstractEntity {

    private static int orderIdNumber;

    private OrderState state;
    private Book book;
    private BigDecimal currentBookPrice;
    private Date creationDate;
    private Date completionDate;
    private String customerData;

    public Order(Book book, String customerData) {
        super(++orderIdNumber);
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
        // убираем бизнес логику из моделей, тут только простейшие геттеры-сеттеры,
        // конструкторы и другие атрибуты модели
        if (state == OrderState.COMPLETED) {
            completionDate = new Date();
        }
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

    public Date getCompletionDate() {
        return completionDate;
    }

    public String getCustomerData() {
        return customerData;
    }

    public void setCustomerData(String customerData) {
        this.customerData = customerData;
    }

    @Override
    public Order clone() {
        Order newOrder = (Order) super.clone();
        newOrder.book = this.book.clone();
        if (this.creationDate != null) {
            newOrder.creationDate = new Date(this.creationDate.getTime());
        }
        if (this.completionDate != null) {
            newOrder.completionDate = new Date(this.completionDate.getTime());
        }
        return newOrder;
    }

    // в следующей домашке будет UI, начинай делать туСтринг более красивым и читабельным
    @Override
    public String toString() {
        return "Order{" +
                "state=" + state +
                ", book=" + book +
                ", currentBookPrice=" + currentBookPrice +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                ", customerData='" + customerData + '\'' +
                ", id=" + id +
                '}';
    }
}
