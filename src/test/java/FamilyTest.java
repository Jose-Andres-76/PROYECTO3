import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyTest {
    @Test
    void testConstructorAndGetters() {
        User father = new User();
        father.setId(1L);
        User son = new User();
        son.setId(2L);

        Family family = new Family(100L, son, father);

        assertEquals(100L, family.getId());
        assertEquals(son, family.getSon());
        assertEquals(father, family.getFather());
    }

    @Test
    void testSetters() {
        Family family = new Family();
        User father = new User();
        father.setId(1L);
        User son = new User();
        son.setId(2L);

        family.setId(200L);
        family.setFather(father);
        family.setSon(son);

        assertEquals(200L, family.getId());
        assertEquals(father, family.getFather());
        assertEquals(son, family.getSon());
    }

    @Test
    void testToString() {
        Family family = new Family();
        family.setId(300L);

        String result = family.toString();
        assertTrue(result.contains("300"));
    }
}