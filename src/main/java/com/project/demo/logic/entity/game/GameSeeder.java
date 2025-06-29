package com.project.demo.logic.entity.game;


import com.project.demo.logic.entity.rol.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class GameSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final GameRepository gameRepository;


    public GameSeeder( GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadGames();
    }

    private void loadGames() {
        GameEnum[] gameNames = new GameEnum[] { GameEnum.ECO_DRAG_DROP, GameEnum.ECO_FILLER, GameEnum.ECO_TRIVIA };
        Map<GameEnum, String> gameDescriptionMap = Map.of(
                GameEnum.ECO_DRAG_DROP, "ECO_DRAG_DROP",
                GameEnum.ECO_FILLER, "ECO_FILLER",
                GameEnum.ECO_TRIVIA, "ECO_TRIVIA"
        );

        Arrays.stream(gameNames).forEach((gameName) -> {
            Optional<Game> optionalGame= gameRepository.findByName(gameName);

            optionalGame.ifPresentOrElse(System.out::println, () -> {
                Game gameToCreate = new Game();

                gameToCreate.setTypesOfGames(gameName);
                gameToCreate.setDescription(gameDescriptionMap.get(gameName));


                gameRepository.save(gameToCreate);
            });
        });
    }
}
