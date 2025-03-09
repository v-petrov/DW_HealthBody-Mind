package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FoodDto {
    @NotNull
    Integer id;
    @NotNull
    String name;
    @NotNull
    int calories;
    @NotNull
    double carbs;
    @NotNull
    double fats;
    @NotNull
    double protein;
    @NotNull
    double sugar;
    @NotNull
    String measurement;
}
