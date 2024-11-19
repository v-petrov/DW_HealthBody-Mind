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
    @NotNull
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Measurement measurement;
    @NotNull
    private int calories;
    @NotNull
    private double carbs;
    @NotNull
    private double fat;
    @NotNull
    private double protein;
    @NotNull
    private double sugar;
}