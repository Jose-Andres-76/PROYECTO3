package com.project.demo.rest.challenge;

import com.project.demo.logic.entity.challenge.Challenge;
import com.project.demo.logic.entity.challenge.ChallengeRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenges")
public class ChallengeRestController {
    @Autowired
    private ChallengeRepository challengeRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllChallenges(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Challenge> challenges = challengeRepository.findAll(pageable).getContent();
        int totalElements = (int) challengeRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(totalPages);
            meta.setTotalElements(totalElements);
            meta.setPageNumber(page);
            meta.setPageSize(size);
            return new GlobalResponseHandler().handleResponse("Challenges listed.", challenges, HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getChallengeById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Challenge> challenge = challengeRepository.findById(id);
        if (challenge.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Challenge found.", challenge.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Challenge with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> createChallenge(@RequestBody Challenge newChallenge, HttpServletRequest request) {
        Challenge savedChallenge = challengeRepository.save(newChallenge);
        return new GlobalResponseHandler().handleResponse("Challenge created successfully.", savedChallenge, HttpStatus.CREATED, request);

    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> updateChallenge(@PathVariable Long id, @RequestBody Challenge challenge, HttpServletRequest request) {
        Optional<Challenge> existingChallengeOpt = challengeRepository.findById(id);
        if (existingChallengeOpt.isPresent()) {
            Challenge existingChallenge = existingChallengeOpt.get();
            if (challenge.getGame() != null) {
                existingChallenge.setGame(challenge.getGame());
            }
            if (challenge.getPoints() != 0) {
                existingChallenge.setPoints(challenge.getPoints());
            }
            if (challenge.isChallengeStatus() != existingChallenge.isChallengeStatus()) {
                existingChallenge.setChallengeStatus(challenge.isChallengeStatus());
            }
            if (challenge.getDescription() != null) {
                existingChallenge.setDescription(challenge.getDescription());
            }
            Challenge updatedChallenge = challengeRepository.save(existingChallenge);
            return new GlobalResponseHandler().handleResponse("Challenge updated successfully.", updatedChallenge, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Challenge with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long id, HttpServletRequest request) {
        Optional<Challenge> challenge = challengeRepository.findById(id);
        if (challenge.isPresent()) {
            challengeRepository.deleteById(id);
            return new GlobalResponseHandler().handleResponse("Challenge deleted successfully.", challenge.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Challenge with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

}
