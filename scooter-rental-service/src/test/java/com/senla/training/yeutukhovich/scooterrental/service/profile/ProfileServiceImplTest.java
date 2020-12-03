package com.senla.training.yeutukhovich.scooterrental.service.profile;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.profile.ProfileDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WithMockUser
class ProfileServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Profile PROFILE = Mockito.mock(Profile.class);
    private static final ProfileDto PROFILE_DTO = Mockito.mock(ProfileDto.class);
    private static final User USER = Mockito.mock(User.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProfileDtoMapper profileDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(PROFILE.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE_DTO.getUserId()).thenReturn(ENTITY_ID);
        Mockito.when(PROFILE.getUser()).thenReturn(USER);
        Mockito.when(USER.getUsername()).thenReturn("user");
    }

    @BeforeEach
    void setUp() {
        Mockito.when(profileDtoMapper.map(PROFILE)).thenReturn(PROFILE_DTO);
        Mockito.when(profileDtoMapper.map(PROFILE_DTO)).thenReturn(PROFILE);
        Mockito.clearInvocations(profileDao);
    }

    @Test
    void ProfileServiceImpl_findAll() {
        profileService.findAll();

        Mockito.verify(profileDao, Mockito.times(1)).findAll();
    }

    @Test
    void ProfileServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));

        ProfileDto profileDto = profileService.findById(ENTITY_ID);

        Assertions.assertNotNull(profileDto);
    }

    @Test
    void ProfileServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> profileService.findById(ENTITY_ID));
    }

    @Test
    void ProfileServiceImpl_deleteById() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));

        ProfileDto profileDto = profileService.deleteById(ENTITY_ID);

        Mockito.verify(profileDao, Mockito.times(1)).delete(PROFILE);
        Assertions.assertNotNull(profileDto);
    }

    @Test
    void ProfileServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> profileService.deleteById(ENTITY_ID));
    }

    @Test
    void ProfileServiceImpl_updateById() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));
        Mockito.when(profileDao.update(PROFILE)).thenReturn(PROFILE);

        ProfileDto profileDto = profileService.updateById(ENTITY_ID, PROFILE_DTO);

        Mockito.verify(profileDao, Mockito.times(1)).update(PROFILE);
        Assertions.assertNotNull(profileDto);
    }

    @Test
    void ProfileServiceImpl_updateById_shouldThrowBusinessException() {
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> profileService.updateById(ENTITY_ID, PROFILE_DTO));
    }

    @Test
    void ProfileServiceImpl_create() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(profileDao.findProfileByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(profileDao.findProfileByPhoneNumber(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(Objects.requireNonNull(USER).getProfile()).thenReturn(null);

        ProfileDto profileDto = profileService.create(PROFILE_DTO);

        Mockito.verify(profileDao, Mockito.times(1)).add(PROFILE);
        Assertions.assertNotNull(profileDto);
    }

    @Test
    void ProfileServiceImpl_create_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> profileService.create(PROFILE_DTO));
    }

    @Test
    void ProfileServiceImpl_create_shouldThrowBusinessExceptionIfUserHasProfile() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(Objects.requireNonNull(USER).getProfile()).thenReturn(PROFILE);

        Assertions.assertThrows(BusinessException.class, () -> profileService.create(PROFILE_DTO));
    }
}
