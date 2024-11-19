package com.diplomawork.healthbody.mind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NutritionsAndGoals {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private int caloriesGoal;
    @NotNull
    private double proteinGoal;
    @NotNull
    private double carbsGoal;
    @NotNull
    private double fatGoal;
    @NotNull
    private int waterGoal;
    @NotNull
    private int stepGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private User user;
}
