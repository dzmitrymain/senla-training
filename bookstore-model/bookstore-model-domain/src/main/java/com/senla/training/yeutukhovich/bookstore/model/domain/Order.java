package com.senla.training.yeutukhovich.bookstore.model.domain;

import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.Date;

@StaticMetamodel(Order.class)
@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Order extends AbstractEntity {

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @ManyToOne
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

    public Order(Book book, String customerData) {
        state = OrderState.CREATED;
        this.book = book;
        currentBookPrice = book.getPrice();
        creationDate = new Date();
        this.customerData = customerData;
    }
}
