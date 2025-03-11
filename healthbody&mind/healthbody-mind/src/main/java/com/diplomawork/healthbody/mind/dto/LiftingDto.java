package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LiftingDto {
    @NotNull
    String workoutActivityLevel;
    @NotNull
    int durationInMinutes;
    @NotNull
    int caloriesBurned;
}
