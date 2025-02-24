package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserProfileDto {
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
    boolean stepsFlag;
}