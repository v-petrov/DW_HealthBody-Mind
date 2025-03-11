package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.Exercise;
import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByUserAndDate(User user, LocalDate date);
}
