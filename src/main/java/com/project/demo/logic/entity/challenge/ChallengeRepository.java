package com.project.demo.logic.entity.challenge;

import com.project.demo.logic.entity.challenge.Challenge;
import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findById(long l);

    @Query ("SELECT c FROM Challenge c WHERE c.gameId = ?1 AND c.familyId.id = ?2")
    Optional<Challenge> findByGameAndFamilyId(Game game, Family familyId);
}
