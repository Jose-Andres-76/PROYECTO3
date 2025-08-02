package com.project.demo.logic.entity.textGame;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TextGameRepository extends JpaRepository<TextGame,Long> {
    @Query ("SELECT tg FROM TextGame tg ORDER BY RAND()")
    List<TextGame> findRandomTextGames(Pageable pageable);
}
