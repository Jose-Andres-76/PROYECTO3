//package com.project.demo.logic.entity.family;
//
//import com.project.demo.logic.entity.user.User;
//import com.project.demo.logic.entity.user.UserRepository;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class FamilySeeder implements ApplicationListener<ContextRefreshedEvent> {
//    private final FamilyRepository familyRepository;
//    private final UserRepository userRepository;
//
//    public FamilySeeder(FamilyRepository familyRepository, UserRepository userRepository) {
//        this.familyRepository = familyRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        this.createFamilies();
//    }
//
//    private void createFamilies() {
//        if (familyRepository.count() == 0) {
//            Family family1 = new Family();
//
//            Optional<User> optionalSon = userRepository.findById(1L);
//            if (optionalSon.isPresent()) {
//                User son = optionalSon.get();
//                family1.setSon(son);
//            } else {
//                family1.setSon(null);
//            }
//
//            Optional<User> optionalFather = userRepository.findById(2L);
//            if (optionalFather.isPresent()) {
//                User father = optionalFather.get();
//                family1.setFather(father);
//            } else {
//                family1.setFather(null);
//            }
//
//            familyRepository.save(family1);
//        }
//    }
//}
