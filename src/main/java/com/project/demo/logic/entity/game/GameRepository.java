package com.project.demo.logic.entity.game;

import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    @Query("SELECT g FROM Game g WHERE g.typesOfGames = ?1")
    Optional<Game> findByName(GameEnum typesOfGames);

}
