import com.project.demo.logic.entity.rol.AdminSeeder;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminSeederTest {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AdminSeeder adminSeeder;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        adminSeeder = new AdminSeeder(roleRepository, userRepository, passwordEncoder);
    }

    @Test
    void shouldNotCreateAdminIfRoleNotExists() {
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.empty());

        adminSeeder.onApplicationEvent(null);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotCreateAdminIfUserAlreadyExists() {
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(new User()));

        adminSeeder.onApplicationEvent(null);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldCreateAdminIfRoleExistsAndUserDoesNotExist() {
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        adminSeeder.onApplicationEvent(null);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Admin", savedUser.getName());
        assertEquals("Admin", savedUser.getLastname());
        assertEquals("admin@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
    }

}