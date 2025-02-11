package com.diplomawork.healthbody.mind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NutritionsAndGoals")
public class NutritionsAndGoals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private int caloriesGoal;

    @Column(nullable = false)
    @NotNull
    private int proteinGoal;

    @Column(nullable = false)
    @NotNull
    private int carbsGoal;

    @Column(nullable = false)
    @NotNull
    private int fatGoal;

    @Column(nullable = false, precision = 3, scale = 1)
    @NotNull
    private BigDecimal waterGoal;

    @Column(nullable = false)
    @NotNull
    private int stepGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId", nullable = false, unique = true)
    @NotNull
    private User user;
}
