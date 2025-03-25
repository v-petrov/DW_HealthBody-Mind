package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.UserRecommendation;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

@Registered
public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Integer> {
    Optional<UserRecommendation> findByUserAndDate(User user, LocalDate date);
}
