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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserProfile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal weight;

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal goalWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private WeeklyGoal weeklyGoal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId", nullable = false, unique = true)
    @NotNull
    private User user;
}