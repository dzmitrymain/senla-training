package com.senla.training.yeutukhovich.bookstore.model.service.user;

import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationRequestDto;
import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationResponseDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.user.UserDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.User;
import com.senla.training.yeutukhovich.bookstore.model.domain.role.UserRole;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public RegistrationResponseDto addUser(RegistrationRequestDto requestDto) {
        if (userDao.findUserByUserName(requestDto.getUsername()).isPresent()) {
            log.info(String.format(MessageConstant.USERNAME_ALREADY_EXISTS.getMessage(),
                    requestDto.getUsername()));
            throw new BusinessException(String.format(MessageConstant.USERNAME_ALREADY_EXISTS.getMessage(),
                    requestDto.getUsername()), HttpStatus.FORBIDDEN);
        }
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(encoder.encode(requestDto.getPassword()));
        try {
            user.setRole(UserRole.valueOf(requestDto.getRole()));
        } catch (IllegalArgumentException e) {
            log.warn(String.format(MessageConstant.ROLE_NOT_EXIST.getMessage(), requestDto.getRole()));
            throw new BusinessException(String.format(MessageConstant.ROLE_NOT_EXIST.getMessage(), requestDto.getRole()),
                    HttpStatus.BAD_REQUEST);
        }
        userDao.add(user);
        log.info(LoggerConstant.USER_REGISTRATION_SUCCESS.getMessage(), user.getUsername());
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        responseDto.setStatus(HttpStatus.OK);
        responseDto.setUsername(user.getUsername());
        responseDto.setRole(user.getRole().name());
        return responseDto;
    }
}
