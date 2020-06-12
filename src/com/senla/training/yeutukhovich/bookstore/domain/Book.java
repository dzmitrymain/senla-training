package com.senla.training.yeutukhovich.bookstore.domain;

import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Book extends AbstractEntity {

    private String title;
    private Boolean isAvailable;
    private Date editionDate;
    private Date replenishmentDate;
    private BigDecimal price;

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
    public Book clone() {
        Book newBook = (Book) super.clone();
        if (this.editionDate != null) {
            newBook.editionDate = new Date(this.editionDate.getTime());
        }
        if (this.replenishmentDate != null) {
            newBook.replenishmentDate = new Date(this.replenishmentDate.getTime());
        }
        return newBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if (!Objects.equals(title, book.title)) {
            return false;
        }
        if (!Objects.equals(editionDate, book.editionDate)) {
            return false;
        }
        if (!Objects.equals(replenishmentDate, book.replenishmentDate)) {
            return false;
        }
        return Objects.equals(price, book.price);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (editionDate != null ? editionDate.hashCode() : 0);
        result = 31 * result + (replenishmentDate != null ? replenishmentDate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", isAvailable=" + isAvailable +
                ", editionDate=" + editionDate +
                ", replenishmentDate=" + replenishmentDate +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
