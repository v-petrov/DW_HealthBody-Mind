package com.diplomawork.healthbody.mind.repository;

import com.diplomawork.healthbody.mind.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
