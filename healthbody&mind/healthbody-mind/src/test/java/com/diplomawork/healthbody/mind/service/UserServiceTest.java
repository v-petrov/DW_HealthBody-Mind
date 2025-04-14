package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.*;
import com.diplomawork.healthbody.mind.exceptions.UserNotFoundException;
import com.diplomawork.healthbody.mind.model.*;
import com.diplomawork.healthbody.mind.model.enums.*;
import com.diplomawork.healthbody.mind.repository.NutritionsAndGoalsRepository;
import com.diplomawork.healthbody.mind.repository.UserProfileRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private NutritionsAndGoalsRepository nutritionsAndGoalsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Spy
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCaloriesAndGoalsWhenFlagTrue() {
        int userId = 1;

        User user = User.builder().id(userId).build();
        UserProfile profile = UserProfile.builder()
                .user(user)
                .gender(Gender.MALE)
                .height(176)
                .weight(BigDecimal.valueOf(79))
                .activityLevel(ActivityLevel.ACTIVE)
                .weeklyGoal(WeeklyGoal.LOSE_0_5_KG)
                .dateOfBirth(LocalDate.of(2002, 2, 14))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.calculateUserCalories(profile, userId, true, 0);

        verify(nutritionsAndGoalsRepository).save(argThat(ng ->
                ng.getUser().equals(user) &&
                        ng.getCaloriesGoal() == 2820 &&
                        ng.getProteinGoal() == 176 &&
                        ng.getCarbsGoal() == 352 &&
                        ng.getFatGoal() == 78 &&
                        ng.getWaterGoal().equals(BigDecimal.valueOf(3)) &&
                        ng.getStepGoal() == 8000
        ));
    }

    @Test
    void testCalculateUserCaloriesWhenFlagTrueSavesCorrectData() {
        int userId = 1;

        User user = User.builder().id(userId).build();
        UserProfile profile = UserProfile.builder()
                .user(user)
                .gender(Gender.MALE)
                .height(176)
                .weight(BigDecimal.valueOf(79))
                .activityLevel(ActivityLevel.ACTIVE)
                .weeklyGoal(WeeklyGoal.LOSE_0_5_KG)
                .dateOfBirth(LocalDate.of(2002, 2, 14))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.calculateUserCalories(profile, userId, true, 0.0);

        ArgumentCaptor<NutritionsAndGoals> captor = ArgumentCaptor.forClass(NutritionsAndGoals.class);
        verify(nutritionsAndGoalsRepository).save(captor.capture());
        NutritionsAndGoals saved = captor.getValue();

        assertEquals(userId, saved.getUser().getId());
        assertEquals(2820, saved.getCaloriesGoal());
        assertEquals(176, saved.getProteinGoal());
        assertEquals(78, saved.getFatGoal());
        assertEquals(352, saved.getCarbsGoal());
    }

    @Test
    void testCalculateUserCaloriesWhenFlagIsFalseShouldCallSaveUserCaloriesWithExpectedValues() {
        int userId = 1;

        User user = User.builder().id(userId).build();
        UserProfile mockProfile = UserProfile.builder()
                .user(user)
                .gender(Gender.MALE)
                .height(176)
                .weight(BigDecimal.valueOf(79))
                .activityLevel(ActivityLevel.ACTIVE)
                .weeklyGoal(WeeklyGoal.LOSE_1_KG)
                .dateOfBirth(LocalDate.of(2002, 2, 14))
                .build();

        double water = 2.5;

        doNothing().when(userService).saveUserCalories(eq(userId), any(CaloriesDto.class));

        userService.calculateUserCalories(mockProfile, userId, false, water);

        ArgumentCaptor<CaloriesDto> dtoCaptor = ArgumentCaptor.forClass(CaloriesDto.class);
        verify(userService).saveUserCalories(eq(userId), dtoCaptor.capture());

        CaloriesDto dto = dtoCaptor.getValue();

        assertEquals(2570, dto.getCalories());
        assertEquals(160, dto.getProtein());
        assertEquals(71, dto.getFats());
        assertEquals(321, dto.getCarbs());
        assertEquals(water, dto.getWater(), 0.001);
    }

    @Test
    void testGetUserCaloriesShouldReturnCorrectCaloriesDto() {
        int userId = 1;
        NutritionsAndGoals goals = NutritionsAndGoals.builder()
                .caloriesGoal(2820)
                .proteinGoal(176)
                .carbsGoal(352)
                .fatGoal(78)
                .waterGoal(BigDecimal.valueOf(3.0))
                .build();

        when(nutritionsAndGoalsRepository.findByUserId(userId)).thenReturn(Optional.of(goals));

        CaloriesDto caloriesDto = userService.getUserCalories(userId);

        assertEquals(2820, caloriesDto.getCalories());
        assertEquals(176, caloriesDto.getProtein());
        assertEquals(352, caloriesDto.getCarbs());
        assertEquals(78, caloriesDto.getFats());
        assertEquals(3.0, caloriesDto.getWater(), 0.001);
    }

    @Test
    void testGetUserDataShouldReturnFullUserDataDtoWhenEverythingExists() {
        int userId = 1;
        User user = User.builder()
                .id(userId)
                .firstName("Vasil")
                .lastName("Petrov")
                .email("totnammm@abv.bg")
                .build();

        UserProfile profile = UserProfile.builder()
                .user(user)
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(2002, 2, 14))
                .height(176)
                .weight(BigDecimal.valueOf(79))
                .goalWeight(BigDecimal.valueOf(69))
                .goal(Goal.LOSE_WEIGHT)
                .weeklyGoal(WeeklyGoal.LOSE_0_5_KG)
                .activityLevel(ActivityLevel.ACTIVE)
                .build();

        NutritionsAndGoals goals = NutritionsAndGoals.builder()
                .user(user)
                .stepGoal(8000)
                .caloriesGoal(2820)
                .proteinGoal(176)
                .carbsGoal(352)
                .fatGoal(78)
                .waterGoal(BigDecimal.valueOf(3.0))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(nutritionsAndGoalsRepository.findByUserId(userId)).thenReturn(Optional.of(goals));

        UserDataDto dto = userService.getUserData(userId);

        assertEquals("Vasil", dto.getFirstName());
        assertEquals("Petrov", dto.getLastName());
        assertEquals("MALE", dto.getGender());
        assertEquals("2002-02-14", dto.getDateOfBirth());
        assertEquals(176, dto.getHeight());
        assertEquals(79.0, dto.getWeight());
        assertEquals(69.0, dto.getGoalWeight());
        assertEquals("LOSE_WEIGHT", dto.getGoal());
        assertEquals("LOSE_0_5_KG", dto.getWeeklyGoal());
        assertEquals("ACTIVE", dto.getActivityLevel());
        assertEquals(8000, dto.getSteps());
        assertEquals(2820, dto.getCalories());
        assertEquals(176, dto.getProtein());
        assertEquals(352, dto.getCarbs());
        assertEquals(78, dto.getFats());
        assertEquals(3.0, dto.getWater(), 0.001);
    }

    @Test
    void testGetUserDataShouldThrowUserNotFoundException() {
        int userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.getUserData(userId));
        assertEquals("User couldn't be found!", thrown.getMessage());
    }

    @Test
    void testGetUserDataShouldThrowRuntimeExceptionWhenUserProfileMissing() {
        int userId = 999;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.getUserData(userId));
        assertEquals("User profile couldn't be found!", thrown.getMessage());
    }

    @Test
    void testGetUserDatShouldThrowRuntimeExceptionWhenNutritionsAndGoalsMissing() {
        int userId = 999;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(new UserProfile()));
        when(nutritionsAndGoalsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.getUserData(userId));
        assertEquals("User goals couldn't be found!", thrown.getMessage());
    }

    @Test
    void testSaveUserProfileShouldUpdateProfileAndGoalsAndReturnCaloriesDto() {
        int userId = 1;
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .weight(70.0)
                .goalWeight(65.0)
                .goal("LOSE_WEIGHT")
                .weeklyGoal("LOSE_0_5_KG")
                .activityLevel("ACTIVE")
                .steps(9000)
                .stepsFlag(false)
                .build();

        UserProfile mockProfile = new UserProfile();
        NutritionsAndGoals mockGoals = new NutritionsAndGoals();
        mockGoals.setWaterGoal(BigDecimal.valueOf(2.5));

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(mockProfile));
        when(nutritionsAndGoalsRepository.findByUserId(userId)).thenReturn(Optional.of(mockGoals));
        CaloriesDto expectedCalories = CaloriesDto.builder()
                .calories(2000)
                .protein(120)
                .carbs(250)
                .fats(70)
                .water(2.5)
                .build();
        when(userService.getUserCalories(userId)).thenReturn(expectedCalories);

        CaloriesDto caloriesDto = userService.saveUserProfile(userId, userProfileDto);

        assertEquals(expectedCalories, caloriesDto);
        assertEquals(9000, mockGoals.getStepGoal());
        verify(userProfileRepository).save(mockProfile);
        verify(nutritionsAndGoalsRepository).save(mockGoals);
        verify(userService).getUserCalories(userId);
    }

    @Test
    void testUpdateProfilePictureShouldUpdateImageUrlAndSaveProfile() {
        int userId = 2;
        String imageUrl = "https://test.bg/profile.jpg";
        UserProfile mockProfile = new UserProfile();

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(mockProfile));

        userService.updateProfilePicture(userId, imageUrl);

        assertEquals(imageUrl, mockProfile.getImageUrl());
        verify(userProfileRepository).save(mockProfile);
    }

    @Test
    void testGetProfilePictureShouldReturnDtoWithImageUrl() {
        int userId = 2;
        UserProfile profile = new UserProfile();
        profile.setImageUrl("https://test.bg/image.jpg");

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        ProfilePictureDto result = userService.getProfilePicture(userId);

        assertEquals("https://test.bg/image.jpg", result.getProfilePictureUrl());
    }

    @Test
    void testGetProfilePictureShouldReturnEmptyUrl() {
        int userId = 2;
        UserProfile profile = new UserProfile();

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        ProfilePictureDto result = userService.getProfilePicture(userId);

        assertEquals("", result.getProfilePictureUrl());
    }

    @Test
    void testChangePasswordShouldUpdatePassword() {
        int userId = 1;
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        ChangingPasswordDto dto = new ChangingPasswordDto("oldPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.changePassword(userId, dto);

        verify(userRepository).save(user);
        assertTrue(new BCryptPasswordEncoder().matches("newPassword", user.getPassword()));
    }

    @Test
    void testChangePasswordShouldThrowIfOldPasswordIncorrect() {
        int userId = 1;
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(userId, new ChangingPasswordDto("wrongPassword", "newPassword")));

        assertEquals("Current password is incorrect!", ex.getMessage());
    }

    @Test
    void testChangeEmailShouldUpdateEmail() {
        int userId = 1;
        String newEmail = "newEmail@abv.bg";

        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        ChangingEmailDto changingEmailDto = new ChangingEmailDto("password", newEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);

        userService.changeEmail(userId, changingEmailDto);

        verify(userRepository).save(user);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testChangeEmailShouldThrowIfPasswordWrong() {
        int userId = 1;
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.changeEmail(userId, new ChangingEmailDto("wrongPassword", "newEmail@abv.bg")));

        assertEquals("We couldn't process your request. Please check your inputs.", ex.getMessage());
    }

    @Test
    void testChangeEmailShouldThrowIfEmailExists() {
        int userId = 1;
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("email@abv.bg")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.changeEmail(userId, new ChangingEmailDto("password", "email@abv.bg")));

        assertEquals("We couldn't process your request. Please check your inputs.", ex.getMessage());
    }
}