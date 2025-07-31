package com.project.demo.logic.entity.textGame;


import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.game.GameEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(5)
@Component
public class TextGameSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final TextGameRepository textGameRepository;
    private final GameRepository gameRepository;

    public TextGameSeeder(TextGameRepository textGameRepository, GameRepository gameRepository) {
        this.textGameRepository = textGameRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createTextGames();
    }

    private void createTextGames() {
        if (textGameRepository.count() == 0) {
            TextGame q1 = new TextGame();
            Optional<Game> optionalGame1 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame1.isPresent()) {
                Game game = optionalGame1.get();
                q1.setGame(game);
            } else {
                q1.setGame(null);
            }
            q1.setText("El contenedor de color verde se usa para reciclar ______.");
            q1.setAnswer("vidrio");
            textGameRepository.save(q1);

            TextGame q2 = new TextGame();
            Optional<Game> optionalGame2 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame2.isPresent()) {
                Game game = optionalGame2.get();
                q2.setGame(game);
            } else {
                q2.setGame(null);
            }
            q2.setText("Reducir, reutilizar y ______ son las tres erres del reciclaje.");
            q2.setAnswer("reciclar");
            textGameRepository.save(q2);

            TextGame q3 = new TextGame();
            Optional<Game> optionalGame3 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame3.isPresent()) {
                Game game = optionalGame3.get();
                q3.setGame(game);
            } else {
                q3.setGame(null);
            }
            q3.setText("La energía que proviene del sol se llama energía ______.");
            q3.setAnswer("solar");
            textGameRepository.save(q3);

            TextGame q4 = new TextGame();
            Optional<Game> optionalGame4 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame4.isPresent()) {
                Game game = optionalGame4.get();
                q4.setGame(game);
            } else {
                q4.setGame(null);
            }
            q4.setText("Los residuos orgánicos deben ir al contenedor de color ______.");
            q4.setAnswer("marrón");
            textGameRepository.save(q4);

            TextGame q5 = new TextGame();
            Optional<Game> optionalGame5 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame5.isPresent()) {
                Game game = optionalGame5.get();
                q5.setGame(game);
            } else {
                q5.setGame(null);
            }
            q5.setText("Los océanos se contaminan principalmente con ______.");
            q5.setAnswer("plástico");
            textGameRepository.save(q5);

            TextGame q6 = new TextGame();
            Optional<Game> optionalGame6 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame6.isPresent()) {
                Game game = optionalGame6.get();
                q6.setGame(game);
            } else {
                q6.setGame(null);
            }
            q6.setText("Reciclar papel ayuda a salvar los ______.");
            q6.setAnswer("árboles");
            textGameRepository.save(q6);

            TextGame q7 = new TextGame();
            Optional<Game> optionalGame7 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame7.isPresent()) {
                Game game = optionalGame7.get();
                q7.setGame(game);
            } else {
                q7.setGame(null);
            }
            q7.setText("Los gases que provocan el calentamiento global se llaman gases de efecto ______.");
            q7.setAnswer("invernadero");
            textGameRepository.save(q7);

            TextGame q8 = new TextGame();
            Optional<Game> optionalGame8 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame8.isPresent()) {
                Game game = optionalGame8.get();
                q8.setGame(game);
            } else {
                q8.setGame(null);
            }
            q8.setText("Una bolsa de plástico puede tardar hasta ______ años en degradarse.");
            q8.setAnswer("400");
            textGameRepository.save(q8);

            TextGame q9 = new TextGame();
            Optional<Game> optionalGame9 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame9.isPresent()) {
                Game game = optionalGame9.get();
                q9.setGame(game);
            } else {
                q9.setGame(null);
            }
            q9.setText("Apagar las luces al salir de una habitación ayuda a ahorrar ______.");
            q9.setAnswer("energía");
            textGameRepository.save(q9);

            TextGame q10 = new TextGame();
            Optional<Game> optionalGame10 = gameRepository.findByName(GameEnum.ECO_FILLER);
            if (optionalGame10.isPresent()) {
                Game game = optionalGame10.get();
                q10.setGame(game);
            } else {
                q10.setGame(null);
            }
            q10.setText("Plantar árboles contribuye a reducir el ______ en la atmósfera.");
            q10.setAnswer("CO2");
            textGameRepository.save(q10);
        }
    }
}
