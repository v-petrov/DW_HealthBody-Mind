package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserDynamicDataDto {
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
    @NotNull
    LocalDate date;
}
