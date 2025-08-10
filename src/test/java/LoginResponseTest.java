import com.project.demo.logic.entity.user.LoginResponse;
import com.project.demo.logic.entity.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {
    @Test
    void testSettersAndGetters() {
        LoginResponse response = new LoginResponse();

        String token = "abc123token";
        long expiresIn = 3600L;
        User user = new User();
        user.setId(1L);


        response.setToken(token);
        response.setExpiresIn(expiresIn);
        response.setAuthUser(user);

        assertEquals(token, response.getToken());
        assertEquals(expiresIn, response.getExpiresIn());
        assertEquals(user, response.getAuthUser());
    }

}