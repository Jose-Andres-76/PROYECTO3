import com.project.demo.logic.entity.challenge.Challenge;
import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.game.Game;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {
    @Test
    void testConstructorAndGetters() {
        Family family = new Family();
        family.setId(1L);

        Game game = new Game();
        game.setId(2L);

        Challenge challenge = new Challenge(10L, family, game, 100, true, "Complete the game");

        assertEquals(10L, challenge.getId());
        assertEquals(family, challenge.getFamily());
        assertEquals(game, challenge.getGame());
        assertEquals(100, challenge.getPoints());
        assertTrue(challenge.isChallengeStatus());
        assertEquals("Complete the game", challenge.getDescription());
    }

    @Test
    void testSetters() {
        Challenge challenge = new Challenge();

        Family family = new Family();
        family.setId(5L);

        Game game = new Game();
        game.setId(6L);

        challenge.setId(20L);
        challenge.setFamily(family);
        challenge.setGame(game);
        challenge.setPoints(200);
        challenge.setChallengeStatus(false);
        challenge.setDescription("New challenge");

        Date createdAt = new Date();
        Date updatedAt = new Date();
        challenge.setCreatedAt(createdAt);
        challenge.setUpdatedAt(updatedAt);

        assertEquals(20L, challenge.getId());
        assertEquals(family, challenge.getFamily());
        assertEquals(game, challenge.getGame());
        assertEquals(200, challenge.getPoints());
        assertFalse(challenge.isChallengeStatus());
        assertEquals("New challenge", challenge.getDescription());
        assertEquals(createdAt, challenge.getCreatedAt());
        assertEquals(updatedAt, challenge.getUpdatedAt());
    }

    @Test
    void testToString() {
        Challenge challenge = new Challenge();
        challenge.setId(50L);
        challenge.setDescription("ToString test");

        String result = challenge.toString();

        assertTrue(result.contains("50"));
        assertTrue(result.contains("ToString test"));
    }

}