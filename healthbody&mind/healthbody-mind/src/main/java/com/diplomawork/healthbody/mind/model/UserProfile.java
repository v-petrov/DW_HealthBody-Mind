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
    @NotNull
    private Goal goal;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ActivityLevel activityLevel;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private int height;
    @NotNull
    private double weight;
    @NotNull
    private double goalWeight;
    @Enumerated(EnumType.STRING)
    @NotNull
    private WeeklyGoal weeklyGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private User user;
}