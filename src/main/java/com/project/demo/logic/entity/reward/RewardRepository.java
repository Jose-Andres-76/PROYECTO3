package com.project.demo.logic.entity.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("SELECT r FROM Reward r WHERE r.familyId.id = ?1")
    List<Reward> findByFamilyId(Long familyId);

    @Query("SELECT r FROM Reward r WHERE r.id = ?1")
    Optional<Reward> findById(Long id);

    @Query("SELECT r FROM Reward r WHERE r.familyId.id IN (SELECT f.id FROM Family f WHERE f.idFather.id = ?1 OR f.idSon.id = ?1)")
    List<Reward> findFamilyRewardsByUserId(Long userId);

}
