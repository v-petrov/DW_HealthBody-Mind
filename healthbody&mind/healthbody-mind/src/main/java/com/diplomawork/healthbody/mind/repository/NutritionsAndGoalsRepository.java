package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.NutritionsAndGoals;
import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionsAndGoalsRepository extends JpaRepository<NutritionsAndGoals, Integer> {
    Optional<NutritionsAndGoals> findByUserId(Integer userId);
}
