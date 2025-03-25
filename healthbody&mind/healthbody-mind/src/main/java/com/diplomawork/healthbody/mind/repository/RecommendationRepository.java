package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.Recommendation;
import com.diplomawork.healthbody.mind.model.enums.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByGoal(Goal goal);
}