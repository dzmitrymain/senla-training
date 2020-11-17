package com.senla.training.yeutukhovich.scooterrental.dao.model;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;

import java.util.List;

public interface ModelDao extends GenericDao<Model, Long> {

    List<Scooter> findScootersByModelId(Long id);

    List<Review> findReviewsByModelId(Long id);

    Rate findCurrentRateByModelId(Long id);
}
