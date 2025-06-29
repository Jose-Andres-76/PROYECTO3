package com.project.demo.logic.entity.user;

import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSeeder (
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createUser();
    }

    private void createUser() {
        User Father = new User();
        Father.setName("Father");
        Father.setLastname("Figure");
        Father.setEmail("father@gmail.com");
        Father.setPassword("father123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.FATHER);
        Optional<User> optionalUser = userRepository.findByEmail(Father.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(Father.getName());
        user.setLastname(Father.getLastname());
        user.setEmail(Father.getEmail());
        user.setPassword(passwordEncoder.encode(Father.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);

        
    }
}
