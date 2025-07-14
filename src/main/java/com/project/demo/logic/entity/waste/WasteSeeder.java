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
            waste1.setProductType("Plastic");
            waste1.setAnswer("This is a recyclable plastic. Please dispose of it in the recycling bin.");
            wasteRepository.save(waste1);

            Waste waste2 = new Waste();
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                waste2.setUser(user);
            } else {
                waste2.setUser(null);
            }
            waste2.setProductType("Metal");
            waste2.setAnswer("This is meta.");
            wasteRepository.save(waste2);

            Waste waste3 = new Waste();
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                waste3.setUser(user);
            } else {
                waste3.setUser(null);
            }
            waste3.setProductType(null);
            waste3.setAnswer("Unable to identify the product type. Please provide more information.");
            wasteRepository.save(waste3);
        }
    }
}