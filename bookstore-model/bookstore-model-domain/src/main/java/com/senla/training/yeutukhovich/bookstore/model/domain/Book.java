package com.senla.training.yeutukhovich.bookstore.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractEntity {

    @Column(name = "title")
    private String title;
    @Column(name = "is_available")
    private Boolean available;
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

    public Book(Long id) {
        this.id = id;
    }
}
