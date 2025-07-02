package com.project.demo.rest.cloudinary;

import com.project.demo.services.cloudinary.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryRestController {

    private final CloudinaryService cloudinaryService;

    public CloudinaryRestController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<Map> uploadImage(@RequestParam("file") MultipartFile file) {
        Map result = cloudinaryService.upload(file);
        return ResponseEntity.ok(result);
    }
}
