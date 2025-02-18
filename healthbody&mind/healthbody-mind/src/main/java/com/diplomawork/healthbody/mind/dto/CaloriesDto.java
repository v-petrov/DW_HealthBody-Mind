package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CaloriesDto {
    @NotNull
    int calories;
    @NotNull
    int protein;
    @NotNull
    int carbs;
    @NotNull
    int fats;
}