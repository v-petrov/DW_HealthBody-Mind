package com.diplomawork.healthbody.mind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class FoodIntakeDto {
    private Long id;
    @NotNull
    private double quantity;
    @NotNull
    private String mealTime;
    @NotNull
    private LocalDate date;
    @NotNull
    private FoodDto foodDto;
}
