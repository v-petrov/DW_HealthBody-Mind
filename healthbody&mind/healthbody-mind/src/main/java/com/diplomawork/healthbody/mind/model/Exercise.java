package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.Type;
import com.diplomawork.healthbody.mind.model.enums.WorkoutActivityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private int duration;
    @NotNull
    private double caloriesBurned;
    private Integer steps;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;
    @Enumerated(EnumType.STRING)
    @NotNull
    private WorkoutActivityLevel workoutActivityLevel;
    @NotNull
    private LocalDate date;

    @ManyToOne
    @NotNull
    private User user;
}