package com.senla.training.yeutukhovich.scooterrental.service.user;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.role.Role;
import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final User USER = Mockito.mock(User.class);
    private static final UserDto USER_DTO = Mockito.mock(UserDto.class);
    private static final Profile PROFILE = Mockito.mock(Profile.class);
    private static final ProfileDto PROFILE_DTO = Mockito.mock(ProfileDto.class);
    private static final RegistrationRequestDto REGISTRATION_REQUEST_DTO = Mockito.mock(RegistrationRequestDto.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Autowired
    private ProfileDtoMapper profileDtoMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void setup() {
        Mockito.when(USER.getId()).thenReturn(ENTITY_ID);
        Mockito.when(USER_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE.getUser()).thenReturn(USER);
        Mockito.when(PROFILE_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE_DTO.getUserId()).thenReturn(ENTITY_ID);
        Mockito.when(USER_DTO.getProfileDto()).thenReturn(PROFILE_DTO);
        Mockito.when(USER.getProfile()).thenReturn(PROFILE);
        Mockito.when(REGISTRATION_REQUEST_DTO.getUserDto()).thenReturn(USER_DTO);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(userDtoMapper.map(USER)).thenReturn(USER_DTO);
        Mockito.when(userDtoMapper.map(USER_DTO)).thenReturn(USER);
        Mockito.when(profileDtoMapper.map(PROFILE)).thenReturn(PROFILE_DTO);
        Mockito.when(profileDtoMapper.map(PROFILE_DTO)).thenReturn(PROFILE);
        Mockito.clearInvocations(userDao);
    }

    @Test
    void UserServiceImpl_changePasswordByUserId() {
        final String oldPassword = "oldPassword";
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(Objects.requireNonNull(USER).getPassword()).thenReturn(oldPassword);
        Mockito.when(passwordEncoder.encode(oldPassword)).thenReturn(oldPassword);
        Mockito.when(userDao.update(USER)).thenReturn(USER);

        UserDto userDto = userService.changePasswordByUserId(ENTITY_ID, oldPassword, "newPassword");

        Mockito.verify(userDao, Mockito.times(1)).update(USER);
        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserServiceImpl_changePasswordByUserId_shouldThrowBusinessExceptionIfOldPasswordIncorrect() {
        final String oldPassword = "oldPassword";
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(Objects.requireNonNull(USER).getPassword()).thenReturn(oldPassword);
        Mockito.when(passwordEncoder.encode(oldPassword)).thenReturn("incorrectOldPassword");
        Mockito.when(userDao.update(USER)).thenReturn(USER);

        Assertions.assertThrows(BusinessException.class,
                () -> userService.changePasswordByUserId(ENTITY_ID, oldPassword, "newPassword"));
    }

    @Test
    void UserServiceImpl_changePasswordByUserId_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class,
                () -> userService.changePasswordByUserId(ENTITY_ID, "oldPassword", "newPassword"));
    }

    @Test
    void UserServiceImpl_findSortedByCreationDateUserRents() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));

        userService.findSortedByCreationDateUserRents(ENTITY_ID);

        Mockito.verify(userDao, Mockito.times(1)).findAllSortedByCreationUserRents(ENTITY_ID);
    }

    @Test
    void UserServiceImpl_findSortedByCreationDateUserRents_shouldThrowBusinessException() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> userService.findSortedByCreationDateUserRents(ENTITY_ID));
    }

    @Test
    void UserServiceImpl_findAllActiveUserPasses() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));

        userService.findAllActiveUserPasses(ENTITY_ID);

        Mockito.verify(userDao, Mockito.times(1)).findAllActiveUserPasses(ENTITY_ID);
    }

    @Test
    void UserServiceImpl_findAllActiveUserPasses_shouldThrowBusinessException() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> userService.findAllActiveUserPasses(ENTITY_ID));
    }

    @Test
    void UserServiceImpl__findAll() {
        userService.findAll();

        Mockito.verify(userDao, Mockito.times(1)).findAll();
    }

    @Test
    void UserServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));

        UserDto userDto = userService.findById(ENTITY_ID);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> userService.findById(ENTITY_ID));
    }

    @Test
    void UserServiceImpl_deleteById() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));

        UserDto discountDtoResult = userService.deleteById(ENTITY_ID);

        Mockito.verify(userDao, Mockito.times(1)).delete(USER);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void UserServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> userService.deleteById(ENTITY_ID));
    }

    @Test
    void UserServiceImpl_updateById() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(userDao.update(USER)).thenReturn(USER);
        Mockito.when(USER_DTO.getRole()).thenReturn(Role.USER.name());

        UserDto discountDtoResult = userService.updateById(ENTITY_ID, USER_DTO);

        Mockito.verify(userDao, Mockito.times(1)).update(USER);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void UserServiceImpl_updateById_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> userService.updateById(ENTITY_ID, USER_DTO));
    }

    @Test
    void UserServiceImpl_register() {
        final String username = "username";
        Mockito.when(USER_DTO.getUsername()).thenReturn(username);
        Mockito.when(userDao.findUserByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(USER_DTO.getRole()).thenReturn(Role.USER.name());
        Mockito.when(Objects.requireNonNull(USER).getProfile()).thenReturn(null);

        UserDto userDto = userService.register(REGISTRATION_REQUEST_DTO);

        Mockito.verify(userDao, Mockito.times(1)).add(USER);
        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserServiceImpl_register_shouldThrowBusinessExceptionIfUsernameAlreadyExists() {
        final String username = "username";
        Mockito.when(USER_DTO.getUsername()).thenReturn(username);
        Mockito.when(userDao.findUserByUsername(username)).thenReturn(Optional.ofNullable(USER));

        Assertions.assertThrows(BusinessException.class, () -> userService.register(REGISTRATION_REQUEST_DTO));
    }

    @Test
    void UserServiceImpl_register_shouldThrowBusinessExceptionIfAdminRegistrationByNoneAdmin() {
        final String username = "username";
        Mockito.when(USER_DTO.getUsername()).thenReturn(username);
        Mockito.when(userDao.findUserByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(USER_DTO.getRole()).thenReturn(Role.ADMIN.name());

        Assertions.assertThrows(BusinessException.class, () -> userService.register(REGISTRATION_REQUEST_DTO));
    }
}
