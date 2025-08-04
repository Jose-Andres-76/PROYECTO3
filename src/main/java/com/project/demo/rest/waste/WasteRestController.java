package com.project.demo.rest.waste;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.waste.Waste;
import com.project.demo.logic.entity.waste.WasteRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> getAllWaste(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        if (page <= 0) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Waste> wastePage = wasteRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(wastePage.getTotalPages());
        meta.setTotalElements(wastePage.getTotalElements());
        meta.setPageNumber(wastePage.getNumber() + 1);
        meta.setPageSize(wastePage.getSize());

        return new GlobalResponseHandler().handleResponse("Waste retrieved successfully",
                wastePage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<?> getWasteById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Waste> waste = wasteRepository.findById(id);
        if (waste.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Waste found",
                    waste.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Waste not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN')")
    public ResponseEntity<?> getWasteByUserId(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("User not found",
                    HttpStatus.NOT_FOUND, request);
        }
        List<Waste> wasteList = wasteRepository.findByUserId(user.get());
        return new GlobalResponseHandler().handleResponse("Waste by user retrieved successfully",
                wasteList, HttpStatus.OK, request);
    }

    @GetMapping("/product-type/{productType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getWasteByProductType(@PathVariable String productType, HttpServletRequest request) {
        List<Waste> wasteList = wasteRepository.findByProductType(productType);
        return new GlobalResponseHandler().handleResponse("Waste by product type retrieved successfully",
                wasteList, HttpStatus.OK, request);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN', 'SON')")
    public ResponseEntity<?> createWaste(@RequestBody WasteCreateRequest request, HttpServletRequest httpRequest) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("User not found",
                    HttpStatus.BAD_REQUEST, httpRequest);
        }

        Waste waste = new Waste();
        waste.setUser(user.get());
        waste.setProductType(request.getProductType());
        waste.setAnswer(request.getAnswer());

        Waste savedWaste = wasteRepository.save(waste);
        return new GlobalResponseHandler().handleResponse("Waste created successfully",
                savedWaste, HttpStatus.CREATED, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN', 'SON')")
    public ResponseEntity<?> updateWaste(@PathVariable Long id, @RequestBody WasteUpdateRequest request, HttpServletRequest httpRequest) {
        Optional<Waste> existingWaste = wasteRepository.findById(id);
        if (existingWaste.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Waste not found",
                    HttpStatus.NOT_FOUND, httpRequest);
        }

        Waste waste = existingWaste.get();
        if (request.getProductType() != null) {
            waste.setProductType(request.getProductType());
        }
        if (request.getAnswer() != null) {
            waste.setAnswer(request.getAnswer());
        }

        Waste updatedWaste = wasteRepository.save(waste);
        return new GlobalResponseHandler().handleResponse("Waste updated successfully",
                updatedWaste, HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteWaste(@PathVariable Long id, HttpServletRequest request) {
        if (!wasteRepository.existsById(id)) {
            return new GlobalResponseHandler().handleResponse("Waste not found",
                    HttpStatus.NOT_FOUND, request);
        }
        wasteRepository.deleteById(id);
        return new GlobalResponseHandler().handleResponse("Waste deleted successfully",
                HttpStatus.OK, request);
    }

    @GetMapping("/stats/user/{userId}")
    @PreAuthorize("hasAnyRole('FATHER', 'ADMIN', 'SON')")
    public ResponseEntity<?> getWasteStatsByUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("User not found",
                    HttpStatus.NOT_FOUND, request);
        }

        long totalCount = wasteRepository.countByUserId(user.get());
        WasteStats stats = new WasteStats(totalCount);
        return new GlobalResponseHandler().handleResponse("Waste stats retrieved successfully",
                stats, HttpStatus.OK, request);
    }

    @GetMapping("/stats/product-type/{productType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getWasteStatsByProductType(@PathVariable String productType, HttpServletRequest request) {
        long count = wasteRepository.countByProductType(productType);
        WasteStats stats = new WasteStats(count);
        return new GlobalResponseHandler().handleResponse("Waste stats by product type retrieved successfully",
                stats, HttpStatus.OK, request);
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
}