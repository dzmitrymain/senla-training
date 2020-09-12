package com.senla.training.yeutukhovich.bookstore.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@StaticMetamodel(Book.class)
@Entity
@Table(name = "books")
public class Book extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "edition_year")
    private int editionYear;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "replenishment_date")
    private Date replenishmentDate;
    @Column(name = "price")
    private BigDecimal price;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Order> orders;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Request> requests;

    public Book() {

    }

    public Book(Long id) {
        this.id = id;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public int getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(int editionYear) {
        this.editionYear = editionYear;
    }

    public Date getReplenishmentDate() {
        return replenishmentDate;
    }

    public void setReplenishmentDate(Date replenishmentDate) {
        this.replenishmentDate = replenishmentDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "Book [id=" + id +
                ", title='" + title +
                "', is available=" + isAvailable +
                ", price=" + price + "]";
    }
}
