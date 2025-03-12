package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.CaloriesDto;
import com.diplomawork.healthbody.mind.dto.UserDataDto;
import com.diplomawork.healthbody.mind.dto.UserProfileDto;
import com.diplomawork.healthbody.mind.exceptions.UserNotFoundException;
import com.diplomawork.healthbody.mind.model.NutritionsAndGoals;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.UserProfile;
import com.diplomawork.healthbody.mind.model.enums.ActivityLevel;
import com.diplomawork.healthbody.mind.model.enums.Gender;
import com.diplomawork.healthbody.mind.model.enums.Goal;
import com.diplomawork.healthbody.mind.model.enums.WeeklyGoal;
import com.diplomawork.healthbody.mind.repository.NutritionsAndGoalsRepository;
import com.diplomawork.healthbody.mind.repository.UserProfileRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UserService {
    private final NutritionsAndGoalsRepository nutritionsAndGoalsRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public CaloriesDto getUserCalories(Integer userId) {
        NutritionsAndGoals caloriesGoals = nutritionsAndGoalsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User calories couldn't be found!"));
        return CaloriesDto.builder()
                .calories(caloriesGoals.getCaloriesGoal())
                .protein(caloriesGoals.getProteinGoal())
                .carbs(caloriesGoals.getCarbsGoal())
                .fats(caloriesGoals.getFatGoal())
                .water(Double.parseDouble(String.valueOf(caloriesGoals.getWaterGoal()))).build();
    }
    public void calculateUserCalories(UserProfile userProfile, Integer userId, boolean flag, double water) {
        BigDecimal weight = userProfile.getWeight();
        int height = userProfile.getHeight();
        int age  = Period.between(userProfile.getDateOfBirth(), LocalDate.now()).getYears();
        ActivityLevel activityLevel = userProfile.getActivityLevel();
        Gender gender = userProfile.getGender();
        WeeklyGoal weeklyGoal = userProfile.getWeeklyGoal();

        double calories;
        final double commonData = 10 * weight.doubleValue() + 6.25 * height;
        calories = calculateCaloriesForGender(gender, commonData, age);
        calories = calculateCaloriesForActivity(activityLevel, calories);
        calories = calculateCaloriesForWeeklyGoal(weeklyGoal, calories);

        int protein = (int) ((calories * 0.25) / 4.0);
        int fats = (int) ((calories * 0.25) / 9.0);
        int carbs = (int) ((calories * 0.50) / 4.0);

        if(flag) {
            saveUserCaloriesAndGoals(userId, (int)calories, protein, fats, carbs);
        } else {
            CaloriesDto caloriesDto = CaloriesDto.builder()
                    .calories((int)calories)
                    .protein(protein)
                    .carbs(carbs)
                    .fats(fats)
                    .water(water).build();
            saveUserCalories(userId, caloriesDto);
        }
    }
    private void saveUserCaloriesAndGoals(Integer userId, int calories, int protein, int fats, int carbs) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        NutritionsAndGoals nutritionsAndGoals = NutritionsAndGoals.builder()
                .caloriesGoal(calories)
                .proteinGoal(protein)
                .carbsGoal(carbs)
                .fatGoal(fats)
                .waterGoal(new BigDecimal(3))
                .stepGoal(8000)
                .user(user).build();
        nutritionsAndGoalsRepository.save(nutritionsAndGoals);
    }
    public UserDataDto getUserData(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile couldn't be found!"));
        NutritionsAndGoals nutritionsAndGoals = nutritionsAndGoalsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User goals couldn't be found!"));
        return UserDataDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(String.valueOf(userProfile.getGender()))
                .dateOfBirth(String.valueOf(userProfile.getDateOfBirth()))
                .height(userProfile.getHeight())
                .weight(Double.parseDouble(String.valueOf(userProfile.getWeight())))
                .goalWeight(Double.parseDouble(String.valueOf(userProfile.getGoalWeight())))
                .goal(String.valueOf(userProfile.getGoal()))
                .weeklyGoal(String.valueOf(userProfile.getWeeklyGoal()))
                .activityLevel(String.valueOf(userProfile.getActivityLevel()))
                .steps(nutritionsAndGoals.getStepGoal())
                .calories(nutritionsAndGoals.getCaloriesGoal())
                .protein(nutritionsAndGoals.getProteinGoal())
                .carbs(nutritionsAndGoals.getCarbsGoal())
                .fats(nutritionsAndGoals.getFatGoal())
                .water(Double.parseDouble(String.valueOf(nutritionsAndGoals.getWaterGoal())))
                .build();
    }
    public void saveUserCalories(Integer userId, CaloriesDto caloriesDto) {
        NutritionsAndGoals nutritionsAndGoals = nutritionsAndGoalsRepository.findByUserId(userId).
                orElseThrow(() -> new RuntimeException("User goals couldn't be found!"));

        nutritionsAndGoals.setCaloriesGoal(caloriesDto.getCalories());
        nutritionsAndGoals.setCarbsGoal(caloriesDto.getCarbs());
        nutritionsAndGoals.setProteinGoal(caloriesDto.getProtein());
        nutritionsAndGoals.setFatGoal(caloriesDto.getFats());
        nutritionsAndGoals.setWaterGoal(BigDecimal.valueOf(caloriesDto.getWater()));
        nutritionsAndGoalsRepository.save(nutritionsAndGoals);
    }
    public CaloriesDto saveUserProfile(Integer userId, UserProfileDto userProfileDto) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile couldn't be found!"));
        userProfile.setWeight(BigDecimal.valueOf(userProfileDto.getWeight()));
        userProfile.setGoalWeight(BigDecimal.valueOf(userProfileDto.getGoalWeight()));
        userProfile.setGoal(Goal.valueOf(userProfileDto.getGoal()));
        userProfile.setWeeklyGoal(WeeklyGoal.valueOf(userProfileDto.getWeeklyGoal()));
        userProfile.setActivityLevel(ActivityLevel.valueOf(userProfileDto.getActivityLevel()));
        userProfileRepository.save(userProfile);

        NutritionsAndGoals nutritionsAndGoals = nutritionsAndGoalsRepository.findByUserId(userId).
                orElseThrow(() -> new RuntimeException("User goals couldn't be found!"));
        nutritionsAndGoals.setStepGoal(userProfileDto.getSteps());
        nutritionsAndGoalsRepository.save(nutritionsAndGoals);

        if(userProfileDto.isStepsFlag()) {
            calculateUserCalories(userProfile, userId, false, Double.parseDouble(String.valueOf(nutritionsAndGoals.getWaterGoal())));
        }
        return getUserCalories(userId);
    }
    public void updateProfilePicture(Integer userId, String imageUrl) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User's profile couldn't be found!"));
        userProfile.setImageUrl(imageUrl);
        userProfileRepository.save(userProfile);
    }
    public String getProfilePicture(Integer userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User's profile couldn't be found!"));

        if (userProfile.getImageUrl() == null || userProfile.getImageUrl().isEmpty()) {
            throw new RuntimeException("No profile picture found.");
        }

        return  userProfile.getImageUrl();
    }
    private double calculateCaloriesForGender(Gender gender, double commonData, int age) {
        double calories;
        switch (gender) {
            case MALE -> calories = commonData - 5 * age + 5;
            case FEMALE -> calories = commonData - 5 * age - 161;
            default -> throw new RuntimeException("Error with Gender enum value");
        }
        return calories;
    }
    private double calculateCaloriesForActivity(ActivityLevel activityLevel, double calories) {
        double caloriesAL = calories;
        switch (activityLevel) {
            case SEDENTARY -> caloriesAL *= 1.2;
            case SLIGHT_ACTIVE -> caloriesAL *= 1.375;
            case MODERATELY_ACTIVE -> caloriesAL *= 1.55;
            case ACTIVE -> caloriesAL *= 1.725;
            case VERY_ACTIVE -> caloriesAL *= 1.9;
            default -> throw new RuntimeException("Error with ActivityLevel enum value");
        }
        return caloriesAL;
    }
    private double calculateCaloriesForWeeklyGoal(WeeklyGoal weeklyGoal, double calories) {
        double caloriesWG = calories;
        switch (weeklyGoal) {
            case LOSE_0_5_KG -> caloriesWG -= 250;
            case LOSE_1_KG -> caloriesWG -= 500;
            case GAIN_0_5_KG -> caloriesWG += 250;
            case GAIN_1_KG -> caloriesWG += 500;
            default -> throw new RuntimeException("Error with WeeklyGoal enum value");
        }
        return caloriesWG;
    }
}