package com.project.demo.logic.entity.waste;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WasteSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    public WasteSeeder(WasteRepository wasteRepository, UserRepository userRepository) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createWaste();
    }

    private void createWaste() {
        if (wasteRepository.count() == 0) {
            Waste waste1 = new Waste();

            Optional<User> optionalUser = userRepository.findById(1L);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                waste1.setUser(user);
            } else {
                waste1.setUser(null);
            }
            waste1.setUrlImage("");
            waste1.setAnswer("This is a sample answer for the waste question.");
            wasteRepository.save(waste1);
        }
    }
}
