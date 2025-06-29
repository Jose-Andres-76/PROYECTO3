//package com.project.demo.logic.entity.reward;
//
//import com.project.demo.logic.entity.family.Family;
//import com.project.demo.logic.entity.family.FamilyRepository;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class RewardSeeder implements ApplicationListener<ContextRefreshedEvent> {
//    private final RewardRepository rewardRepository;
//    private final FamilyRepository familyRepository;
//
//    public RewardSeeder(RewardRepository rewardRepository, FamilyRepository familyRepository) {
//        this.rewardRepository = rewardRepository;
//        this.familyRepository = familyRepository;
//    }
////
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        this.createRewards();
//    }
//
//    private void createRewards() {
//        if (rewardRepository.count() == 0) {
//            Reward reward1 = new Reward();
//
//            Optional<Family> optionalFamily = familyRepository.findById(1L);
//            if (optionalFamily.isPresent()) {
//                Family family = optionalFamily.get();
//                reward1.setFamily(family);
//            } else {
//                reward1.setFamily(null);
//            }
//            reward1.setCost(5);
//            reward1.setDescription("A brand new bicycle.");
//            reward1.setStatus(false);
//
//            rewardRepository.save(reward1);
//        }
//    }
//}
