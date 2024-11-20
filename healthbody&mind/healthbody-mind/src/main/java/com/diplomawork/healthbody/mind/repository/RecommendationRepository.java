package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}