package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@StaticMetamodel(Rate.class)
@Entity
@Table(name = "rates")
@Data
@EqualsAndHashCode(callSuper = true, of = {"model", "creationDate"})
public class Rate extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @Column(name = "per_hour")
    private BigDecimal perHour;
    @Column(name = "weekend_per_hour")
    private BigDecimal weekendPerHour;
    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;
}
