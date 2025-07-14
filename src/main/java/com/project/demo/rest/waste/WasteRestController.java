package com.project.demo.rest.waste;

import com.project.demo.logic.entity.waste.Waste;
import com.project.demo.logic.entity.waste.WasteRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/waste")
public class WasteRestController {

    @Autowired
    private WasteRepository wasteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Waste> getAllWaste(Pageable pageable) {
        return wasteRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<Waste> getWasteById(@PathVariable Long id) {
        Optional<Waste> waste = wasteRepository.findById(id);
        return waste.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<List<Waste>> getWasteByUserId(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Waste> wasteList = wasteRepository.findByUserId(user.get());
        return ResponseEntity.ok(wasteList);
    }

    @GetMapping("/product-type/{productType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Waste>> getWasteByProductType(@PathVariable String productType) {
        List<Waste> wasteList = wasteRepository.findByProductType(productType);
        return ResponseEntity.ok(wasteList);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<Waste> createWaste(@RequestBody WasteCreateRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Waste waste = new Waste();
        waste.setUser(user.get());
        waste.setProductType(request.getProductType());
        waste.setAnswer(request.getAnswer());

        Waste savedWaste = wasteRepository.save(waste);
        return ResponseEntity.ok(savedWaste);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<Waste> updateWaste(@PathVariable Long id, @RequestBody WasteUpdateRequest request) {
        Optional<Waste> existingWaste = wasteRepository.findById(id);
        if (existingWaste.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Waste waste = existingWaste.get();
        if (request.getProductType() != null) {
            waste.setProductType(request.getProductType());
        }
        if (request.getAnswer() != null) {
            waste.setAnswer(request.getAnswer());
        }

        Waste updatedWaste = wasteRepository.save(waste);
        return ResponseEntity.ok(updatedWaste);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWaste(@PathVariable Long id) {
        if (!wasteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        wasteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/user/{userId}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<WasteStats> getWasteStatsByUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        long totalCount = wasteRepository.countByUserId(user.get());
        WasteStats stats = new WasteStats(totalCount);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/product-type/{productType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WasteStats> getWasteStatsByProductType(@PathVariable String productType) {
        long count = wasteRepository.countByProductType(productType);
        WasteStats stats = new WasteStats(count);
        return ResponseEntity.ok(stats);
    }

    public static class WasteCreateRequest {
        private Long userId;
        private String productType;
        private String answer;

        public WasteCreateRequest() {}

        public WasteCreateRequest(Long userId, String productType, String answer) {
            this.userId = userId;
            this.productType = productType;
            this.answer = answer;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getProductType() { return productType; }
        public void setProductType(String productType) { this.productType = productType; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }

    public static class WasteUpdateRequest {
        private String productType;
        private String answer;

        public WasteUpdateRequest() {}

        public WasteUpdateRequest(String productType, String answer) {
            this.productType = productType;
            this.answer = answer;
        }

        public String getProductType() { return productType; }
        public void setProductType(String productType) { this.productType = productType; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }

    public static class WasteStats {
        private long totalCount;

        public WasteStats(long totalCount) {
            this.totalCount = totalCount;
        }

        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
    }

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
