package com.senla.training.yeutukhovich.bookstore.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Book extends AbstractEntity {

    private static final long serialVersionUID = -5485469789503724920L;

    private String title;
    private Boolean isAvailable;
    private int editionYear;
    private Date replenishmentDate;
    private BigDecimal price;

    public Book() {

    }

    public Book(Long id) {
        this.id = id;
    }

    public Book(String title, Boolean isAvailable, int editionYear, Date replenishmentDate, BigDecimal price) {
        this.title = title;
        this.setAvailable(isAvailable);
        this.replenishmentDate = replenishmentDate;
        this.editionYear = editionYear;
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

    @Override
    public String toString() {
        return "Book [id=" + id +
                ", title='" + title +
                "', is available=" + isAvailable +
                ", price=" + price + "]";
    }
}
