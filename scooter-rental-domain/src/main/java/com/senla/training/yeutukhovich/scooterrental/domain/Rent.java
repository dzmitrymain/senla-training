package com.senla.training.yeutukhovich.scooterrental.domain;

import com.senla.training.yeutukhovich.scooterrental.domain.type.PaymentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rents")
@Data
@EqualsAndHashCode(callSuper = true, of = {"user", "scooter"})
public class Rent extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "scooter_id")
    private Scooter scooter;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
    @Column(name = "return_date")
    private LocalDateTime returnDate;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Column(name = "distance_travelled")
    private Integer distance_travelled;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "overtime_penalty")
    private BigDecimal overtimePenalty;
}
