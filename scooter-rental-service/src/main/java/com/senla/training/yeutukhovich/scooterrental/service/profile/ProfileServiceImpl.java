package com.senla.training.yeutukhovich.scooterrental.service.profile;

import com.senla.training.yeutukhovich.scooterrental.dao.profile.ProfileDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class ProfileServiceImpl implements ProfileService {

    private static final String ENTITY_NAME = "Profile";

    private final ProfileDao profileDao;
    private final UserDao userDao;
    private final ProfileDtoMapper profileDtoMapper;

    @Autowired
    public ProfileServiceImpl(ProfileDao profileDao,
                              UserDao userDao,
                              ProfileDtoMapper profileDtoMapper) {
        this.profileDao = profileDao;
        this.userDao = userDao;
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
    public ProfileDto updateById(Long id, @Valid ProfileDto profileDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        Profile checkedProfile = findProfileById(id);
        checkUserMatch(checkedProfile.getUser());
        profileDto.setUserId(checkedProfile.getUser().getId());
        if (!id.equals(profileDto.getId())) {
            profileDto.setId(id);
        }
        Profile profileToUpdate = profileDtoMapper.map(profileDto);
        checkUniqueness(profileToUpdate);
        return profileDtoMapper.map(profileDao.update(profileToUpdate));
    }

    @Override
    @Transactional
    public ProfileDto create(@Valid ProfileDto profileDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Profile profile = profileDtoMapper.map(profileDto);
        profile.setUser(userDao.findById(profileDto.getUserId()).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), "User"), HttpStatus.NOT_FOUND)));

        if (profile.getUser().getProfile() != null) {
            throw new BusinessException(String.format(ExceptionConstant.PROFILE_ALREADY_EXIST.getMessage(),
                    profileDto.getUserId()), HttpStatus.FORBIDDEN);
        }
        profile.setId(null);
        checkUniqueness(profile);
        profileDao.add(profile);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, profile.getId());
        return profileDtoMapper.map(profile);
    }

    private Profile findProfileById(Long profileId) {
        return profileDao.findById(profileId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }

    private void checkUserMatch(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !user.getUsername().equals(auth.getName())) {
            throw new AccessDeniedException(ExceptionConstant.USER_ACCESS_DENIED.getMessage());
        }
    }

    private void checkUniqueness(Profile profile) {
        profileDao.findProfileByEmail(profile.getEmail()).ifPresent(checkedProfile -> {
            if (checkedProfile.getId().equals(profile.getId())) {
                return;
            }
            throw new BusinessException(String.format(ExceptionConstant.PROFILE_EMAIL_ALREADY_EXISTS.getMessage(),
                    profile.getEmail()), HttpStatus.FORBIDDEN);
        });
        profileDao.findProfileByPhoneNumber(profile.getPhoneNumber()).ifPresent(checkedProfile -> {
            if (checkedProfile.getId().equals(profile.getId())) {
                return;
            }
            throw new BusinessException(String.format(ExceptionConstant.PROFILE_PHONE_NUMBER_ALREADY_EXISTS.getMessage(),
                    profile.getPhoneNumber()), HttpStatus.FORBIDDEN);
        });
    }
}
