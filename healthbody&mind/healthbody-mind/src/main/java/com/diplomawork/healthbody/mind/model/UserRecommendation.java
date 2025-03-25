package com.diplomawork.healthbody.mind.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserRecommendation",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"})})
public class UserRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String filledRecommendation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}