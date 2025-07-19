package com.project.demo.rest.game;

import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameRestController {
    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllGames(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Game> games = gameRepository.findAll(pageable).getContent();
        int totalElements = (int) gameRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Games listed.", games, HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getGameById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Game found.", game.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Game with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }
}
