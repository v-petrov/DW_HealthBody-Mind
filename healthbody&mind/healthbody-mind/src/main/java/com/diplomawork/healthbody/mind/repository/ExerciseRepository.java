package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.Exercise;
import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByUserAndDate(User user, LocalDate date);
    @Query("SELECT new map(e.date AS date, SUM(e.caloriesBurned) AS burnedCalories) " +
            "FROM Exercise e " +
            "WHERE e.user.id = :userId " +
            "AND e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.date ORDER BY e.date ASC")
    List<Map<String, Object>> getBurnedCaloriesByDate(Integer userId, LocalDate startDate, LocalDate endDate);
}
