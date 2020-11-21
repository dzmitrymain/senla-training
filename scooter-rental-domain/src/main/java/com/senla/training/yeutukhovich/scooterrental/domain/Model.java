package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "models")
@Data
@EqualsAndHashCode(callSuper = true, of = "modelName")
public class Model extends AbstractEntity {

    @Column(name = "name")
    private String modelName;
    @Column(name = "[range]")
    private Short range;
    @Column(name = "speed")
    private Short speed;
    @Column(name = "power")
    private Short power;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Rate> rates;
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Pass> passes;
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Discount> discounts;
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Scooter> scooters;
}
