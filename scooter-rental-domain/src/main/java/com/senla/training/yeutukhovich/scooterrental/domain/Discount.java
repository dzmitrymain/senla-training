package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Data
@EqualsAndHashCode(callSuper = true)
public class Discount extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "discount")
    private BigDecimal discount;
}
