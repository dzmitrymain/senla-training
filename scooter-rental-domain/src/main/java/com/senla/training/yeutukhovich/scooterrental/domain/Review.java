package com.senla.training.yeutukhovich.scooterrental.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@EqualsAndHashCode(callSuper = true, of = "profile")
public class Review extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @Column(name = "score")
    private Byte score;
    @Column(name = "opinion")
    private String opinion;
    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;
}
