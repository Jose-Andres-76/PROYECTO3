package com.project.demo.logic.entity.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(long id);

    @Query("SELECT g FROM Game g WHERE g.typesOfGames = ?1")
    Optional<Game> findByName(GameEnum typesOfGames);
}
