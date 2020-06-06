package com.senla.training.yeutukhovich.bookstore.service.dto;


import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.util.Date;

public class BookDescription {

    private String title;
    private Date editionDate;

    public BookDescription() {

    }

    public BookDescription(String title, Date editionDate) {
        this.title = title;
        this.editionDate = editionDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }

    @Override
    public String toString() {
        return "BookDescription{" +
                "title='" + title + '\'' +
                ", editionDate=" + DateConverter.formatDate(editionDate, DateConverter.YEAR_DATE_FORMAT) +
                '}';
    }
}
