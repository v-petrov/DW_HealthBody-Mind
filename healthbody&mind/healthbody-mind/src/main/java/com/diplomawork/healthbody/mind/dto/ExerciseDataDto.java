package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExerciseDataDto {
    @NotNull
    int caloriesBurnedL;
    @NotNull
    int caloriesBurnedC;
    @NotNull
    int hoursC;
    @NotNull
    int minutesC;
    @NotNull
    int dailySteps;
}
