package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "scooters")
@Data
@EqualsAndHashCode(callSuper = true)
public class Scooter extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;
    @Column(name = "begin_operation_date")
    private LocalDateTime beginOperationDate;
    @Column(name = "total_distance_travelled")
    private Integer totalDistanceTravelled;

    @OneToMany(mappedBy = "scooter")
    private Set<Rent> rents;
}