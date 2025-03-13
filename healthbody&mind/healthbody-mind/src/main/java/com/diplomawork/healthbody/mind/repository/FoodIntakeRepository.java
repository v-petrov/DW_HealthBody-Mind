package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.FoodIntake;
import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface FoodIntakeRepository extends JpaRepository<FoodIntake, Long> {
    List<FoodIntake> findByUserAndDate(User user, LocalDate date);
    @Query("SELECT new map(fi.date AS date, SUM((fi.quantity / 100) * f.calories) AS caloriesIntake) " +
            "FROM FoodIntake fi " +
            "JOIN fi.food f " +
            "WHERE fi.user.id = :userId " +
            "AND fi.date BETWEEN :startDate AND :endDate " +
            "GROUP BY fi.date ORDER BY fi.date ASC")
    List<Map<String, Object>> getCaloriesIntakeByDate(Integer userId, LocalDate startDate, LocalDate endDate);
    @Query("SELECT new map(fi.date AS date, SUM((fi.quantity / 100) * f.carbs) AS carbsIntake) " +
            "FROM FoodIntake fi " +
            "JOIN fi.food f " +
            "WHERE fi.user.id = :userId " +
            "AND fi.date BETWEEN :startDate AND :endDate " +
            "GROUP BY fi.date ORDER BY fi.date ASC")
    List<Map<String, Object>> getCarbsIntakeByDate(Integer userId, LocalDate startDate, LocalDate endDate);
    @Query("SELECT new map(fi.date AS date, SUM((fi.quantity / 100) * f.fat) AS fatIntake) " +
            "FROM FoodIntake fi " +
            "JOIN fi.food f " +
            "WHERE fi.user.id = :userId " +
            "AND fi.date BETWEEN :startDate AND :endDate " +
            "GROUP BY fi.date ORDER BY fi.date ASC")
    List<Map<String, Object>> getFatIntakeByDate(Integer userId, LocalDate startDate, LocalDate endDate);
    @Query("SELECT new map(fi.date AS date, SUM((fi.quantity / 100) * f.protein) AS proteinIntake) " +
            "FROM FoodIntake fi " +
            "JOIN fi.food f " +
            "WHERE fi.user.id = :userId " +
            "AND fi.date BETWEEN :startDate AND :endDate " +
            "GROUP BY fi.date ORDER BY fi.date ASC")
    List<Map<String, Object>> getProteinIntakeByDate(Integer userId, LocalDate startDate, LocalDate endDate);
}