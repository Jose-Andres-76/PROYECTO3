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

    public CloudinaryRestController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            HttpServletRequest request) {

        return new GlobalResponseHandler().handleResponse("Im using Cloudinary",
                 HttpStatus.OK, request);
    }

    /**
     *
     * @param file
     *  This param what it does is to upload de the image that you implement
     *
     */

//    @PostMapping("/upload")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
//        return ResponseEntity.ok(cloudinaryService.upload(file));
//    }

    @PostMapping("/user/{id}")
    public ResponseEntity<?> upload(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file
    ) {
        return ResponseEntity.ok(cloudinaryService.upload(id, file) );
    }

//
//    /**
//     *
//     * @param publicId
//     * @param file
//     * This two what they do is Basically call the exact image from the public id from cloudinary
//     */
//    @PutMapping("/update/{publicId}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Map> updateImage(
//            @PathVariable String publicId,
//            @RequestParam("file") MultipartFile file) {
//        return ResponseEntity.ok(cloudinaryService.updateImage(file, publicId));
//    }
//
//
//    /**
//     *
//     * @param publicId
//     * This one allocated the exact image that we can to change in cloudinary
//     */
//
//    @GetMapping("/image/{publicId}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<String> getImage(@PathVariable String publicId) {
//        return ResponseEntity.ok(cloudinaryService.getImageUrl(publicId));
//    }
//
//    /**
//     *
//     * @param publicId
//     * This method deletes the image from the server
//     */
//    @DeleteMapping("/image/{publicId}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Map> deleteImage(@PathVariable String publicId) {
//        return ResponseEntity.ok(cloudinaryService.delete(publicId));
//    }
//
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
