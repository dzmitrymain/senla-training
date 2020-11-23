package com.senla.training.yeutukhovich.scooterrental.service.user;

import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.Role;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.UserDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.PassDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.UserDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.profile.ProfileService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String ENTITY_NAME = "User";

    private final UserDao userDao;

    private final ProfileService profileService;

    private final UserDtoMapper userDtoMapper;
    private final ProfileDtoMapper profileDtoMapper;
    private final PassDtoMapper passDtoMapper;
    private final RentDtoMapper rentDtoMapper;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ProfileService profileService,
                           UserDtoMapper userDtoMapper,
                           ProfileDtoMapper profileDtoMapper, PassDtoMapper passDtoMapper,
                           RentDtoMapper rentDtoMapper,
                           PasswordEncoder encoder) {
        this.userDao = userDao;
        this.profileService = profileService;
        this.userDtoMapper = userDtoMapper;
        this.profileDtoMapper = profileDtoMapper;
        this.passDtoMapper = passDtoMapper;
        this.rentDtoMapper = rentDtoMapper;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public UserDto register(RegistrationRequestDto registrationRequestDto) {
        if (userDao.findUserByUsername(registrationRequestDto.getUserDto().getUsername()).isPresent()) {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.USER_ALREADY_EXIST.getMessage(),
                            registrationRequestDto.getUserDto().getUsername()), HttpStatus.FORBIDDEN);
            log.warn(exception.getMessage());
            throw exception;
        }
        if (registrationRequestDto.getUserDto().getRole().toUpperCase().equals(Role.ADMIN.name())) {
            if (!checkAdmin()) {
                BusinessException exception = new BusinessException(ExceptionConstant.USER_CREATE_ADMIN_FAIL.getMessage(),
                        HttpStatus.FORBIDDEN);
                log.warn(exception.getMessage());
                throw exception;
            }
        }
        User user = userDtoMapper.map(registrationRequestDto.getUserDto());
        user.setId(null);
        user.setEnabled(true);
        user.setPassword(encoder.encode(registrationRequestDto.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        Profile profile = user.getProfile();
        user.setProfile(null);
        userDao.add(user);
        if (profile != null) {
            profile.setUser(user);
            user.setProfile(profileDtoMapper.map(profileService.create(profileDtoMapper.map(profile))));
        }
        return userDtoMapper.map(user);
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return userDao.findAll().stream()
                .map(userDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return userDtoMapper.map(findUserById(id));
    }

    @Override
    @Transactional
    public UserDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        User userToDelete = findUserById(id);
        userDao.delete(userToDelete);
        return userDtoMapper.map(userToDelete);
    }

    @Override
    @Transactional
    public UserDto updateById(Long id, UserDto userDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        User user = findUserById(id);
        user.setEnabled(userDto.getEnabled());
        return userDtoMapper.map(userDao.update(user));
    }


    @Override
    @Transactional
    public UserDto changePasswordByUserId(Long id, String oldPassword, String newPassword) {
        log.info(LoggerConstant.USER_CHANGE_PASSWORD.getMessage(), id);
        User user = findUserById(id);
        if (!user.getPassword().equals(encoder.encode(oldPassword))) {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.USER_CHANGE_PASSWORD_WRONG.getMessage(), id), HttpStatus.FORBIDDEN);
            log.warn(exception.getMessage());
            throw exception;
        }
        user.setPassword(encoder.encode(newPassword));
        return userDtoMapper.map(userDao.update(user));
    }

    @Override
    @Transactional
    public List<RentDto> findSortedByCreationDateUserRents(Long id) {
        log.info(LoggerConstant.USER_RENTS.getMessage(), id);
        return userDao.findAllSortedByCreationUserRents(id).stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<PassDto> findAllActiveUserPasses(Long id) {
        log.info(LoggerConstant.USER_PASSES.getMessage(), id);
        return userDao.findAllActiveUserPasses(id).stream()
                .map(passDtoMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }

    private boolean checkAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            return false;
        }
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
