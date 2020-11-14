package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "locations")
@Data
@EqualsAndHashCode(callSuper = true)
public class Location extends AbstractEntity {

    @Column(name = "name")
    private String locationName;
}
