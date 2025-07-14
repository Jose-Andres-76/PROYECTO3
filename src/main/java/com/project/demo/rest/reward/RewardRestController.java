package com.project.demo.rest.reward;


import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.reward.Reward;
import com.project.demo.logic.entity.reward.RewardRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rewards")
public class RewardRestController {
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<?> getFamilyRewardsByUserId(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Reward> rewards = rewardRepository.findFamilyRewardsByUserId(userId);
            return new GlobalResponseHandler().handleResponse("Rewards for family found.", rewards, HttpStatus.OK, request);
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
}
