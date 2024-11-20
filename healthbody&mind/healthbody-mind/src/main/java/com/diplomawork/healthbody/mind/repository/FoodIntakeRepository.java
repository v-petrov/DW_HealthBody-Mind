package com.diplomawork.healthbody.mind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodIntakeRepository extends JpaRepository<FoodIntakeRepository,Long> {
}
