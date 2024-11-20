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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull
    private int duration;

    @Column(nullable = false)
    @NotNull
    private double caloriesBurned;

    private Integer steps;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private WorkoutActivityLevel workoutActivityLevel;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne
    @NotNull
    private User user;
}