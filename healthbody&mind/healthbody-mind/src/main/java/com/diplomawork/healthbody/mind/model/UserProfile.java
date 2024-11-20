package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.ActivityLevel;
import com.diplomawork.healthbody.mind.model.enums.Gender;
import com.diplomawork.healthbody.mind.model.enums.Goal;
import com.diplomawork.healthbody.mind.model.enums.WeeklyGoal;
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
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Goal goal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Gender gender;

    @Column(nullable = false)
    @NotNull
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @NotNull
    private int height;

    @Column(nullable = false)
    @NotNull
    private double weight;

    @Column(nullable = false)
    @NotNull
    private double goalWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private WeeklyGoal weeklyGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private User user;
}