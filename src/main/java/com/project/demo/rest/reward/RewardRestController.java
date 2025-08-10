package com.project.demo.rest.reward;


import com.project.demo.logic.entity.email.EmailModel;
import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.reward.Reward;
import com.project.demo.logic.entity.reward.RewardRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.services.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rewards")
public class RewardRestController {
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllRewards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Reward> rewards = rewardRepository.findAll(pageable).getContent();
        int totalElements = (int) rewardRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Rewards listed.", rewards, HttpStatus.OK, meta);
    }

    @GetMapping("active/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllActiveRewards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long id,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Reward> rewards = rewardRepository.findSonActiveRewardsByUserId(id);
        int totalElements = (int) rewardRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);

        System.out.println(rewards.size());

        if(rewards.size()==0){
            List<Reward> rewardList = new ArrayList<>();
            return new GlobalResponseHandler().handleResponse("Active Rewards not listed.", rewardList, HttpStatus.OK, meta);
        }
        return new GlobalResponseHandler().handleResponse("Rewards listed.", rewards, HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRewardById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Reward> reward = rewardRepository.findById(id);
        if (reward.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Reward found.", reward.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Reward with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/my-family-rewards/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFamilyRewardsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Reward> allRewards = rewardRepository.findFamilyRewardsByUserId(userId);
            int totalElements = allRewards.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            List<Reward> rewards = allRewards.subList(startIndex, endIndex);

            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(totalPages);
            meta.setTotalElements(totalElements);
            meta.setPageNumber(page);
            meta.setPageSize(size);

            return new GlobalResponseHandler().handleResponse("Rewards for family found.", rewards, HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User with id: " + userId + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> createReward(@RequestBody Reward newReward, HttpServletRequest request) {
        Reward savedReward = rewardRepository.save(newReward);
        return new GlobalResponseHandler().handleResponse("Reward created successfully.", savedReward, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> updateReward(@PathVariable Long id, @RequestBody Reward reward, HttpServletRequest request) {
        Optional<Reward> existingRewardOpt = rewardRepository.findById(id);
        if (existingRewardOpt.isPresent()) {
            Reward existingReward = existingRewardOpt.get();
            if (reward.getFamily() != null) {
                existingReward.setFamily(reward.getFamily());
            }
            if (reward.getCost() != 0) {
                existingReward.setCost(reward.getCost());
            }
            if (reward.getDescription() != null) {
                existingReward.setDescription(reward.getDescription());
            }
            if (reward.isStatus() != existingReward.isStatus()) {
                existingReward.setStatus(reward.isStatus());
            }
            Reward savedReward = rewardRepository.save(existingReward);
            return new GlobalResponseHandler().handleResponse("Reward updated successfully.", savedReward, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Reward with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }


    @PutMapping("redeem/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> redeemReward(@PathVariable Long id, HttpServletRequest request) {
        Optional<Reward> existingRewardOpt = rewardRepository.findById(id);


        if (existingRewardOpt.isPresent()) {
            Reward existingReward = existingRewardOpt.get();
            Family family = existingReward.getFamily();
            Optional<User> existingSon=userRepository.findById(family.getSon().getId());
            User son=existingSon.get();
            int pointTotal=son.getPoints()-existingReward.getCost();
            if(pointTotal<0){
                return new GlobalResponseHandler().handleResponse("Reward Not Avaible to Reedeem", existingReward, HttpStatus.NOT_FOUND, request);
            } else{
                existingReward.setStatus(false);
                Family father = existingReward.getFamily();
                EmailModel emailPadre=SendEmailReedemReward(existingReward,father);
                emailService.sendSimpleEmail(emailPadre);
                rewardRepository.save(existingReward);
                son.setPoints(pointTotal);
                userRepository.save(son);
            }
            return new GlobalResponseHandler().handleResponse("Reward updated successfully.", existingReward, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Reward with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }




    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> deleteReward(@PathVariable Long id, HttpServletRequest request) {
        Optional<Reward> reward = rewardRepository.findById(id);
        if (reward.isPresent()) {
            rewardRepository.delete(reward.get());
            return new GlobalResponseHandler().handleResponse("Reward deleted successfully.", reward.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Reward with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public EmailModel SendEmailReedemReward(Reward reward, Family family){
        EmailModel emailModel = new EmailModel();
        emailModel.setTo(family.getFather().getEmail());
        emailModel.setSubject("Canjeo de Puntos Eco de su Hijo " + family.getSon().getName() + " "+ family.getSon().getLastname());
        emailModel.setText("Su hijo/a acaba de reclamar el siguiente premio " + reward.getDescription() + " el mismo se esforzo mucho porque tuvo que gastar este numero de puntos: "+ reward.getCost());
        return emailModel;


    }
}
