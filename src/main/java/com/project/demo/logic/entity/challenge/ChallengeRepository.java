package com.project.demo.logic.entity.challenge;

import com.project.demo.logic.entity.family.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Family> findById(long l);
}
