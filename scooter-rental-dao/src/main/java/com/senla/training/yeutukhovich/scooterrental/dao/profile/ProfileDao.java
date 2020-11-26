package com.senla.training.yeutukhovich.scooterrental.dao.profile;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;

import java.util.Optional;

public interface ProfileDao extends GenericDao<Profile, Long> {

    Optional<Profile> findProfileByEmail(String email);

    Optional<Profile> findProfileByPhoneNumber(String phoneNumber);
}
