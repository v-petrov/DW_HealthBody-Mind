package com.diplomawork.healthbody.mind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutritionsAndGoalsRepository extends JpaRepository<NutritionsAndGoalsRepository, UUID> {
}
