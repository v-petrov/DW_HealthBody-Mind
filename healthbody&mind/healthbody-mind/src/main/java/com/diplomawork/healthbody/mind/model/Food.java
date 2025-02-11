package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.Measurement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal carbs;

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal fat;

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal protein;

    @Column(nullable = false, precision = 4, scale = 1)
    @NotNull
    private BigDecimal sugar;
}