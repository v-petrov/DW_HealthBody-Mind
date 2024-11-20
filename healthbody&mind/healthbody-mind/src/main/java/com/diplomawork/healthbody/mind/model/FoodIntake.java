package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.MealTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodIntake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull
    private double quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MealTime mealTime;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Food food;
}