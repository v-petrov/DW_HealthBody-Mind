package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.MealTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FoodIntake")
public class FoodIntake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 6, scale = 2)
    @NotNull
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MealTime mealTime;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    @NotNull
    private Food food;
}