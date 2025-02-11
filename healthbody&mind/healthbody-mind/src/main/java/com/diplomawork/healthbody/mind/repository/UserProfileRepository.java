package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
}
