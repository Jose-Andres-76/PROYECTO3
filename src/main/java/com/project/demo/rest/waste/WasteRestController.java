package com.project.demo.rest.waste;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.waste.Waste;
import com.project.demo.logic.entity.waste.WasteRepository;
import com.project.demo.rest.user.UserRestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waste")
public class WasteRestController {
    @Autowired
    private WasteRepository wasteRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllWaste(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Waste> wasteList = wasteRepository.findAll(pageable).getContent();
        int totalElements = (int) wasteRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Waste listed.", wasteList, HttpStatus.OK, meta);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWasteByUser(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Waste> wasteList = wasteRepository.findByUserId(userId, pageable);
        int totalElements = wasteRepository.countByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Waste for user listed.", wasteList, HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createWaste(@RequestBody Waste newWaste, HttpServletRequest request) {
        Waste savedWaste = wasteRepository.save(newWaste);
        return new GlobalResponseHandler().handleResponse("Waste created successfully.", savedWaste, HttpStatus.CREATED, request);
    }
}
