package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.FoodIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodIntakeRepository extends JpaRepository<FoodIntake, Long> {
}
