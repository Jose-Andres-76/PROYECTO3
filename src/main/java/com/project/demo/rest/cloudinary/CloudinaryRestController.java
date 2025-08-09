package com.project.demo.rest.cloudinary;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.services.cloudinary.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryRestController {

    @Autowired
    private CloudinaryService cloudinaryService;

    public CloudinaryRestController() {
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            HttpServletRequest request) {

        return new GlobalResponseHandler().handleResponse("Im using Cloudinary",
                 HttpStatus.OK, request);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<?> upload(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file
    ) {
        return ResponseEntity.ok(cloudinaryService.upload(id, file) );
    }


    @PutMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(cloudinaryService.overwrite(id,file));
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
