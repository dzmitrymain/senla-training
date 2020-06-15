package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Order extends AbstractEntity {

    private OrderState state;
    private Book book;
    private BigDecimal currentBookPrice;
    private Date creationDate;
    private Date completionDate;
    private String customerData;

    public Order(Book book, String customerData) {
        super(IdGenerator.getInstance().getNextOrderIdNumber());
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (state != order.state) {
            return false;
        }
        if (!Objects.equals(book, order.book)) {
            return false;
        }
        if (!Objects.equals(currentBookPrice, order.currentBookPrice)) {
            return false;
        }
        if (!Objects.equals(creationDate, order.creationDate)) {
            return false;
        }
        return Objects.equals(customerData, order.customerData);
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (currentBookPrice != null ? currentBookPrice.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (customerData != null ? customerData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order [id=" + id +
                ", state=" + state +
                ", customer data='" + customerData +
                "', book title='" + book.getTitle() + "']";
    }
}
