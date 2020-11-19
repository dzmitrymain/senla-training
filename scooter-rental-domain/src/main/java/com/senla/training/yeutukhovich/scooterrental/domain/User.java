package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true, of = "username")
public class User extends AbstractEntity {

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;
    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Rent> rents;
    @OneToMany(mappedBy = "user")
    private Set<Pass> passes;
    @OneToOne(mappedBy = "user")
    private Profile profile;
}
