package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "passes")
@Data
@EqualsAndHashCode(callSuper = true, of = {"user", "model", "creationDate"})
public class Pass extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
    @Column(name = "total_minutes")
    private Integer totalMinutes;
    @Column(name = "remaining_minutes")
    private Integer remainingMinutes;
    @Column(name = "price")
    private BigDecimal price;
}
