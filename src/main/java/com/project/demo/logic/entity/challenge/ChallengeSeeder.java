package com.project.demo.logic.entity.challenge;

import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.family.FamilyRepository;
import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameEnum;
import com.project.demo.logic.entity.game.GameRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChallengeSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final ChallengeRepository challengeRepository;
    private final FamilyRepository familyRepository;
    private final GameRepository gameRepository;

    public ChallengeSeeder(ChallengeRepository challengeRepository,
                           FamilyRepository familyRepository,
                           GameRepository gameRepository) {
        this.challengeRepository = challengeRepository;
        this.familyRepository = familyRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createChallenges();
    }

    private void createChallenges() {
        if (challengeRepository.count() == 0) {
            Challenge challenge1 = new Challenge();

            Optional<Family> optionalFamily = familyRepository.findById(1L);
            if (optionalFamily.isPresent()) {
                Family family = optionalFamily.get();
                challenge1.setFamily(family);
            } else {
                challenge1.setFamily(null);
            }

            Optional <Game> optionalGame = gameRepository.findByName(GameEnum.ECO_TRIVIA);
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                challenge1.setGame(game);
            } else {
                challenge1.setGame(null);
            }

            challenge1.setPoints(10);
            challenge1.setChallengeStatus(false);
            challenge1.setDescription("Complete the ECO TRIVIA game with less than 2 errors to earn points.");

            challengeRepository.save(challenge1);
        }

    }

}
