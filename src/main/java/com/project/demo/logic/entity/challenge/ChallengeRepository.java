package com.project.demo.logic.entity.challenge;

import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findById(long l);

    @Query("SELECT c FROM Challenge c WHERE c.gameId = ?1 AND c.familyId.id = ?2")
    Optional<Challenge> findByGameAndFamilyId(Game game, Family familyId);

    @Query("SELECT c FROM Challenge c WHERE c.familyId.id IN (SELECT f.id FROM Family f WHERE f.idFather.id = ?1 OR f.idSon.id = ?1)")
    List<Challenge> findByFamilyId_UserId(Long userId);
}
