package com.senla.training.yeutukhovich.scooterrental.service.profile;

import com.senla.training.yeutukhovich.scooterrental.dao.profile.ProfileDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private static final String ENTITY_NAME = "Profile";

    private final ProfileDao profileDao;
    private final ProfileDtoMapper profileDtoMapper;

    @Autowired
    public ProfileServiceImpl(ProfileDao profileDao, ProfileDtoMapper profileDtoMapper) {
        this.profileDao = profileDao;
        this.profileDtoMapper = profileDtoMapper;
    }

    @Override
    @Transactional
    public List<ProfileDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return profileDao.findAll().stream()
                .map(profileDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfileDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return profileDtoMapper.map(findProfileById(id));
    }

    @Override
    @Transactional
    public ProfileDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Profile profileToDelete = findProfileById(id);
        profileDao.delete(profileToDelete);
        return profileDtoMapper.map(profileToDelete);
    }

    @Override
    @Transactional
    public ProfileDto updateById(Long id, ProfileDto profileDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        Profile checkedProfile = findProfileById(id);
        profileDto.setUserId(checkedProfile.getUser().getId());
        Profile profileToUpdate = profileDtoMapper.map(profileDto);
        checkOnUniqueness(profileToUpdate);
        if (!id.equals(profileDto.getId())) {
            profileDto.setId(id);
        }
        return profileDtoMapper.map(profileDao.update(profileToUpdate));
    }

    @Override
    @Transactional
    public ProfileDto create(ProfileDto profileDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Profile profile = profileDtoMapper.map(profileDto);
        profile.setId(null);
        checkOnUniqueness(profile);
        profileDao.add(profile);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), profile.getId());
        return profileDtoMapper.map(profile);
    }

    @Override
    @Transactional
    public ProfileDto findProfileByEmail(String email) {
        return profileDtoMapper.map(profileDao.findProfileByEmail(email).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    @Override
    @Transactional
    public ProfileDto findProfileByPhoneNumber(String phoneNumber) {
        return profileDtoMapper.map(profileDao.findProfileByPhoneNumber(phoneNumber).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    private Profile findProfileById(Long profileId) {
        return profileDao.findById(profileId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }

    private void checkOnUniqueness(Profile profile) {
        profileDao.findProfileByEmail(profile.getEmail()).ifPresent(checkedProfile -> {
            if (checkedProfile.getId().equals(profile.getId())) {
                return;
            }
            logAndThrowBusinessException(
                    String.format(ExceptionConstant.PROFILE_EMAIL_ALREADY_EXISTS.getMessage(), profile.getEmail()
                    ));
        });
        profileDao.findProfileByPhoneNumber(profile.getPhoneNumber()).ifPresent(checkedProfile -> {
            if (checkedProfile.getId().equals(profile.getId())) {
                return;
            }
            logAndThrowBusinessException(
                    String.format(ExceptionConstant.PROFILE_PHONE_NUMBER_ALREADY_EXISTS.getMessage(), profile.getPhoneNumber()
                    ));
        });
    }

    private void logAndThrowBusinessException(String message) {
        log.warn(message);
        throw new BusinessException(message, HttpStatus.FORBIDDEN);
    }
}
