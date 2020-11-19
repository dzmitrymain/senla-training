package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "locations")
@Data
@EqualsAndHashCode(callSuper = true, of="locationName")
public class Location extends AbstractEntity {

    @Column(name = "name")
    private String locationName;

    @OneToMany(mappedBy = "location")
    private Set<Spot> spots;
    @OneToMany(mappedBy = "location")
    private Set<Profile> profiles;
}
