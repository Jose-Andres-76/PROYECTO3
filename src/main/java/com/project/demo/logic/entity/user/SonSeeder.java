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

public class SonSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public SonSeeder (
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
        this.createSon();
    }

    private void createSon() {

        User Son = new User();
        Son.setName("Son");
        Son.setLastname("Figure");
        Son.setEmail("son@gmail.com");
        Son.setPassword("son123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SON);
        Optional<User> optionalUser = userRepository.findByEmail(Son.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(Son.getName());
        user.setLastname(Son.getLastname());
        user.setEmail(Son.getEmail());
        user.setPassword(passwordEncoder.encode(Son.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
