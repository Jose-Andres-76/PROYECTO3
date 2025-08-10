import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.reward.Reward;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {
    @Test
    void testConstructorAndGetters() {
        Family family = new Family();
        family.setId(1L);

        Reward reward = new Reward(10L, family, 50, "Test reward", true);

        assertEquals(10L, reward.getId());
        assertEquals(family, reward.getFamily());
        assertEquals(50, reward.getCost());
        assertEquals("Test reward", reward.getDescription());
        assertTrue(reward.isStatus());
    }

    @Test
    void testSetters() {
        Reward reward = new Reward();
        Family family = new Family();
        family.setId(2L);

        reward.setId(20L);
        reward.setFamily(family);
        reward.setCost(100);
        reward.setDescription("New reward");
        reward.setStatus(false);

        Date createdAt = new Date();
        Date updatedAt = new Date();
        reward.setCreatedAt(createdAt);
        reward.setUpdatedAt(updatedAt);

        assertEquals(20L, reward.getId());
        assertEquals(family, reward.getFamily());
        assertEquals(100, reward.getCost());
        assertEquals("New reward", reward.getDescription());
        assertFalse(reward.isStatus());
        assertEquals(createdAt, reward.getCreatedAt());
        assertEquals(updatedAt, reward.getUpdatedAt());
    }

    @Test
    void testToString() {
        Reward reward = new Reward();
        reward.setId(30L);
        reward.setDescription("Sample");

        String result = reward.toString();
        assertTrue(result.contains("30"));
        assertTrue(result.contains("Sample"));
    }
}