import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void testConstructorAndGetters() {
        Game game = new Game(1L, GameEnum.QUIZ);
        game.setDescription("Trivia game for kids");

        assertEquals(1L, game.getId());
        assertEquals(GameEnum.QUIZ, game.getTypesOfGames());
        assertEquals("Trivia game for kids", game.getDescription());
    }

    @Test
    void testSetters() {
        Game game = new Game();
        game.setId(2L);
        game.setTypesOfGames(GameEnum.MEMORY);
        game.setDescription("Memory card matching");

        assertEquals(2L, game.getId());
        assertEquals(GameEnum.MEMORY, game.getTypesOfGames());
        assertEquals("Memory card matching", game.getDescription());
    }

    @Test
    void testToString() {
        Game game = new Game(3L, GameEnum.QUIZ);
        game.setDescription("Educational quiz");

        String expected = "Game{id=3, typesOfGames=QUIZ, description='Educational quiz'}";
        assertEquals(expected, game.toString());
    }
}
