package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.Measurement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Measurement measurement;

    @Column(nullable = false)
    @NotNull
    private int calories;

    @Column(nullable = false)
    @NotNull
    private double carbs;

    @Column(nullable = false)
    @NotNull
    private double fat;

    @Column(nullable = false)
    @NotNull
    private double protein;

    @Column(nullable = false)
    @NotNull
    private double sugar;
}