package com.senla.training.yeutukhovich.bookstore.model.domain;

import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "state")
    private String state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "price")
    private BigDecimal currentBookPrice;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "completion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;
    @Column(name = "customer_data")
    private String customerData;

    public Order() {

    }

    public Order(Book book, String customerData) {
        state = OrderState.CREATED.toString();
        this.book = book;
        currentBookPrice = book.getPrice();
        creationDate = new Date();
        this.customerData = customerData;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public OrderState getState() {
        return OrderState.valueOf(state);
    }

    public void setState(OrderState state) {
        this.state = state.toString();
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
                ", customer data='" + customerData + "']";
    }
}
