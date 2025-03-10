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
@Table(name = "Exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private int durationInMinutes;

    @Column(nullable = false)
    @NotNull
    private int caloriesBurned;

    private Integer steps;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Type type;

    @Enumerated(EnumType.STRING)
    private WorkoutActivityLevel workoutActivityLevel;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @NotNull
    private User user;
}