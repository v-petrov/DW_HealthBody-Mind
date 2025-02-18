package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.CaloriesDto;
import com.diplomawork.healthbody.mind.model.NutritionsAndGoals;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.UserProfile;
import com.diplomawork.healthbody.mind.model.enums.ActivityLevel;
import com.diplomawork.healthbody.mind.model.enums.Gender;
import com.diplomawork.healthbody.mind.model.enums.WeeklyGoal;
import com.diplomawork.healthbody.mind.repository.NutritionsAndGoalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UserService {
    private final NutritionsAndGoalsRepository nutritionsAndGoalsRepository;

    public CaloriesDto getUserCalories(Integer userId) {
        NutritionsAndGoals caloriesGoals = nutritionsAndGoalsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User calories couldn't be found!"));
        return CaloriesDto.builder()
                .calories(caloriesGoals.getCaloriesGoal())
                .protein(caloriesGoals.getProteinGoal())
                .carbs(caloriesGoals.getCarbsGoal())
                .fats(caloriesGoals.getFatGoal()).build();
    }
    public void calculateUserCalories(UserProfile userProfile, User user) {
        BigDecimal weight = userProfile.getWeight();
        int height = userProfile.getHeight();
        int age  = Period.between(userProfile.getDateOfBirth(), LocalDate.now()).getYears();
        ActivityLevel activityLevel = userProfile.getActivityLevel();
        Gender gender = userProfile.getGender();
        WeeklyGoal weeklyGoal = userProfile.getWeeklyGoal();

        double calories;
        final double commonData = 10 * weight.doubleValue() + 6.25 * height;
        switch (gender) {
            case MALE -> calories = commonData - 5 * age + 5;
            case FEMALE -> calories = commonData - 5 * age - 161;
            default -> throw new RuntimeException("Error with Gender enum value");
        }
        switch (activityLevel) {
            case SEDENTARY -> calories *= 1.2;
            case SLIGHT_ACTIVE -> calories *= 1.375;
            case MODERATELY_ACTIVE -> calories *= 1.55;
            case ACTIVE -> calories *= 1.725;
            case VERY_ACTIVE -> calories *= 1.9;
            default -> throw new RuntimeException("Error with ActivityLevel enum value");
        }
        switch (weeklyGoal) {
            case LOSE_0_5_KG -> calories -= 250;
            case LOSE_1_KG -> calories -= 500;
            case GAIN_0_5_KG -> calories += 250;
            case GAIN_1_KG -> calories += 500;
            default -> throw new RuntimeException("Error with WeeklyGoal enum value");
        }
        int protein = (int) ((calories * 0.25) / 4.0);
        int fats = (int) ((calories * 0.25) / 9.0);
        int carbs = (int) ((calories * 0.50) / 4.0);

        saveUserCaloriesAndGoals(user, (int)calories, protein, fats, carbs);
    }
    private void saveUserCaloriesAndGoals(User user, int calories, int protein, int fats, int carbs) {
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
}