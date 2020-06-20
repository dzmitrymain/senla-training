package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.math.BigDecimal;
import java.util.Date;

public class Book extends AbstractEntity {

    private String title;
    private Boolean isAvailable;
    private Date editionDate;
    private Date replenishmentDate;
    private BigDecimal price;

    public Book(Long id) {
        super(id);
    }

    public Book(String title, Boolean isAvailable, Date editionDate, Date replenishmentDate, BigDecimal price) {
        super(IdGenerator.getInstance().getNextBookIdNumber());
        this.title = title;
        this.setAvailable(isAvailable);
        this.replenishmentDate = replenishmentDate;
        this.editionDate = editionDate;
        this.price = price;
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

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
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

    @Override
    public String toString() {
        return "Book [id=" + id +
                ", title='" + title +
                "', is available=" + isAvailable +
                ", price=" + price + "]";
    }
}
