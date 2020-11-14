package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "spots")
@Data
@EqualsAndHashCode(callSuper = true)
public class Spot extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "coordinates")
    private Point coordinates;
}
