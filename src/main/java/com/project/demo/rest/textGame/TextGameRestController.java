package com.project.demo.rest.textGame;


import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.textGame.TextGame;
import com.project.demo.logic.entity.textGame.TextGameRepository;
import com.project.demo.logic.entity.http.Meta;
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
@RequestMapping("/text-games")
public class TextGameRestController {
    @Autowired
    private TextGameRepository textGameRepository;

    @GetMapping("/random")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneRandomTextGame() {
        List<TextGame> allGames = textGameRepository.findAll();
        if (allGames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No text games available");
        }

        int idx = (int) (Math.random() * allGames.size());
        TextGame randomGame = allGames.get(idx);

        List<String> allAnswers = new java.util.ArrayList<>(
                allGames.stream()
                        .map(TextGame::getAnswer)
                        .filter(ans -> !ans.equalsIgnoreCase(randomGame.getAnswer()))
                        .distinct()
                        .toList()
        );
        java.util.Collections.shuffle(allAnswers);
        List<String> options = allAnswers.stream().limit(4).collect(java.util.stream.Collectors.toList());
        options.add(randomGame.getAnswer());
        java.util.Collections.shuffle(options);

        return ResponseEntity.ok(new java.util.HashMap<>() {{
            put("question", randomGame.getText());
            put("options", options);
            put("answer", randomGame.getAnswer());
        }});
    }

}
