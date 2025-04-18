package com.diplomawork.healthbody.mind.model;

import com.diplomawork.healthbody.mind.model.enums.Goal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String recommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Goal goal;
}