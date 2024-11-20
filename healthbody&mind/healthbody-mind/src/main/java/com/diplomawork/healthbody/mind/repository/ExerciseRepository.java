package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
