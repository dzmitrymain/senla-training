package com.senla.training.yeutukhovich.bookstore.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Request.class)
@Entity
@Table(name = "requests")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Request extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "is_active")
    private Boolean active;
    @Column(name = "requester_data")
    private String requesterData;

    public Request(Book book, String requesterData) {
        this.requesterData = requesterData;
        this.book = book;
        active = true;
    }
}
