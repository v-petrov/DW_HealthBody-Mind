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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String recommendation;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @NotNull
    private User user;
}