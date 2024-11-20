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

    @Column(nullable = false)
    @NotNull
    private int caloriesGoal;

    @Column(nullable = false)
    @NotNull
    private double proteinGoal;

    @Column(nullable = false)
    @NotNull
    private double carbsGoal;

    @Column(nullable = false)
    @NotNull
    private double fatGoal;

    @Column(nullable = false)
    @NotNull
    private int waterGoal;

    @Column(nullable = false)
    @NotNull
    private int stepGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private User user;
}
