import com.project.demo.logic.entity.email.EmailModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailModelTest {
    @Test
    void testConstructorAndGetters() {
        EmailModel email = new EmailModel("to@example.com", "Test Subject", "This is the body.");

        assertEquals("to@example.com", email.getTo());
        assertEquals("Test Subject", email.getSubject());
        assertEquals("This is the body.", email.getText());
    }

    @Test
    void testSetters() {
        EmailModel email = new EmailModel();

        email.setTo("receiver@example.com");
        email.setSubject("Hello");
        email.setText("Hi there!");

        assertEquals("receiver@example.com", email.getTo());
        assertEquals("Hello", email.getSubject());
        assertEquals("Hi there!", email.getText());
    }

    @Test
    void testToString() {
        EmailModel email = new EmailModel();
        email.setTo("abc@test.com");
        email.setSubject("Sub");
        email.setText("Content");

        String str = email.toString();

        assertTrue(str.contains("abc@test.com"));
        assertTrue(str.contains("Sub"));
        assertTrue(str.contains("Content"));
    }

}