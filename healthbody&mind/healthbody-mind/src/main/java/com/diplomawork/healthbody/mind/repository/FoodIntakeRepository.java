package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.FoodIntake;
import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodIntakeRepository extends JpaRepository<FoodIntake, Long> {
    List<FoodIntake> findByUserAndDate(User user, LocalDate date);
}
