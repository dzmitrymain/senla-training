package com.senla.training.yeutukhovich.bookstore.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Book extends AbstractEntity {

    private static int bookIdNumber;

    private String title;
    // начинаем забывать про примитивы, используем объекты
    private boolean isAvailable;
    private Date editionDate;
    private Date replenishmentDate;
    private BigDecimal price;

    public Book(String title, boolean isAvailable, Date editionDate, BigDecimal price) {
        // очевидная, но, как считается, неправильная реализация айди счетчика
        // про айдишки поговорим отдельно
        // подсказка: нарушение принципа единственной ответственности SOLID
        super(++bookIdNumber);
        this.title = title;
        this.setAvailable(isAvailable);
        this.editionDate = editionDate;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        if (available) {
            replenishmentDate = new Date();
        }
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
        if (isAvailable != book.isAvailable) {
            return false;
        }
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
        result = 31 * result + (isAvailable ? 1 : 0);
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
