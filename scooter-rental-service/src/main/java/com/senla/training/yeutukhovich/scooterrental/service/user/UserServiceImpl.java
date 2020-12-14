package com.senla.training.yeutukhovich.scooterrental.service.user;

import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.role.Role;
import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.PassDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.profile.ProfileService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.scooterrental.validator.marker.OnUserCreate;
import com.senla.training.yeutukhovich.scooterrental.validator.marker.OnUserUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
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
                           ProfileDtoMapper profileDtoMapper,
                           PassDtoMapper passDtoMapper,
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
    @Validated(OnUserCreate.class)
    public UserDto register(@Valid RegistrationRequestDto registrationRequestDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        if (userDao.findUserByUsername(registrationRequestDto.getUserDto().getUsername()).isPresent()) {
            throw new BusinessException(String.format(ExceptionConstant.USER_ALREADY_EXIST.getMessage(),
                    registrationRequestDto.getUserDto().getUsername()), HttpStatus.FORBIDDEN);
        }
        if (registrationRequestDto.getUserDto().getRole().equalsIgnoreCase(Role.ADMIN.name()) && !checkAdmin()) {
            throw new BusinessException(ExceptionConstant.USER_CREATE_ADMIN_FAIL.getMessage(), HttpStatus.FORBIDDEN);
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
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, user.getId());
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
    @Validated(OnUserUpdate.class)
    public UserDto updateById(Long id, @Valid UserDto userDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        User user = findUserById(id);
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setEnabled(userDto.getEnabled());
        return userDtoMapper.map(userDao.update(user));
    }


    @Override
    @Transactional
    public UserDto changePasswordByUserId(Long id, String oldPassword, String newPassword) {
        log.info(LoggerConstant.USER_CHANGE_PASSWORD.getMessage(), id);
        User user = findUserById(id);
        checkUserMatch(user);
        if (!user.getPassword().equals(encoder.encode(oldPassword))) {
            throw new BusinessException(
                    String.format(ExceptionConstant.USER_CHANGE_PASSWORD_WRONG.getMessage(), id), HttpStatus.FORBIDDEN);
        }
        user.setPassword(encoder.encode(newPassword));
        return userDtoMapper.map(userDao.update(user));
    }

    @Override
    @Transactional
    public List<RentDto> findSortedByCreationDateUserRents(Long id) {
        log.info(LoggerConstant.USER_RENTS.getMessage(), id);
        checkUserMatch(findUserById(id));
        return userDao.findAllSortedByCreationUserRents(id).stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<PassDto> findAllActiveUserPasses(Long id) {
        log.info(LoggerConstant.USER_PASSES.getMessage(), id);
        checkUserMatch(findUserById(id));
        return userDao.findAllActiveUserPasses(id).stream()
                .map(passDtoMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }

    private void checkUserMatch(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !user.getUsername().equals(auth.getName())) {
            throw new AccessDeniedException(ExceptionConstant.USER_ACCESS_DENIED.getMessage());
        }
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
