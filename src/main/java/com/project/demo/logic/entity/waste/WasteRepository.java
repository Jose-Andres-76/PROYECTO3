package com.project.demo.logic.entity.waste;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WasteRepository extends JpaRepository<Waste, Long> {
    @Query("SELECT w FROM Waste w WHERE w.userId.id = ?1")
    List<Waste> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(w) FROM Waste w WHERE w.userId.id = ?1")
    int countByUserId(Long userId);
}
