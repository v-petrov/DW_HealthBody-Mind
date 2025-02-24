package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDataDto {
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    String gender;
    @NotNull
    String dateOfBirth;
    @NotNull
    int height;
    @NotNull
    double weight;
    @NotNull
    double goalWeight;
    @NotNull
    String goal;
    @NotNull
    String weeklyGoal;
    @NotNull
    String activityLevel;
    @NotNull
    int steps;
    @NotNull
    int calories;
    @NotNull
    int protein;
    @NotNull
    int carbs;
    @NotNull
    int fats;
    @NotNull
    double water;
}