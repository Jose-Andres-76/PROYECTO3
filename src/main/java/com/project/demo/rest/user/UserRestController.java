package com.project.demo.rest.user;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.user.UserUpdateRequest;
import com.project.demo.services.Utils.RegexChecker;
import com.project.demo.services.cloudinary.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RegexChecker regexChecker;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> ordersPage = userRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Users retrieved successfully",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("User updated successfully",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);

        if(foundUser.isPresent()) {
            User updateUser= foundUser.get();

            updateUser.setName(user.getName());
            updateUser.setLastname(user.getLastname());
            updateUser.setEmail(user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            updateUser.setPoints(user.getPoints());
            updateUser.setAge(user.getAge());
            if (user.getRole() != null && user.getRole().getId() != null) {
                Role role = roleRepository.findById(user.getRole().getId())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                updateUser.setRole(role);
            }
            userRepository.save(updateUser);
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    updateUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @PatchMapping(value = "/editProfilePicture/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserProfilePicture(
            @PathVariable Long userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {

        System.out.println("HTTP REQUES");
        System.out.println(userUpdateRequest);

        Optional<User> foundUser = userRepository.findById(userId);

        if (foundUser.isPresent()) {
            User updateUser = foundUser.get();

            MultipartFile file = userUpdateRequest.getImage();

            if (updateUser.getUrlImage() == null || updateUser.getUrlImage().isEmpty() || regexChecker.checkGoogleImage(updateUser.getUrlImage())) {
                updateUser = cloudinaryService.upload(userId, file);
            } else {
                updateUser = cloudinaryService.overwrite(userId, file);
            }

            System.out.println("ACTUALIZANDO PROFILE");

            updateUser.setName(userUpdateRequest.getName());
            updateUser.setLastname(userUpdateRequest.getLastname());
            updateUser.setAge(userUpdateRequest.getAge());
            if(userUpdateRequest.getPassword() !=null){
                if(!passwordEncoder.matches(userUpdateRequest.getPasswordConfirmation(), updateUser.getPassword())){
                    System.out.println("NO MATCH");
                    return new GlobalResponseHandler().handleResponse("User Password Error",
                            updateUser, HttpStatus.BAD_REQUEST, request);
                }
                updateUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

            }

            userRepository.save(updateUser);

            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    updateUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PatchMapping(value = "/editProfile/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {

        System.out.println("HTTP REQUES NO PIC");
        System.out.println(userUpdateRequest);

        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User updateUser = foundUser.get();

            System.out.println("ACTUALIZANDO PROFILE");

            updateUser.setName(userUpdateRequest.getName());
            updateUser.setLastname(userUpdateRequest.getLastname());
            updateUser.setAge(userUpdateRequest.getAge());
            if(userUpdateRequest.getPassword() !=null){
                if(!passwordEncoder.matches(userUpdateRequest.getPasswordConfirmation(), updateUser.getPassword())){
                    System.out.println("NO MATCH");
                    return new GlobalResponseHandler().handleResponse("User Password Error",
                            updateUser, HttpStatus.BAD_REQUEST, request);
                }
                updateUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

            }

            userRepository.save(updateUser);

            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    updateUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }



    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Order id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    @PatchMapping("/{id}/points")
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER', 'SON')")
    public ResponseEntity<?> updateUserPoints(@PathVariable Long id, @RequestBody Map<String, Integer> payload, HttpServletRequest request) {
        if (!payload.containsKey("points")) {
            return ResponseEntity.badRequest().body("El campo 'points' es obligatorio");
        }

        int newPoints = payload.get("points");

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User user = optionalUser.get();
        user.setPoints(newPoints);
        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse("Puntos actualizados correctamente",
               HttpStatus.OK, request);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER', 'SON')")
    public ResponseEntity<?> getUserById(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Usuario encontrado", userOptional.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse(
                    "Usuario no encontrado", HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER', 'SON')")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "User found", userOptional.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse(
                    "User not found", HttpStatus.NOT_FOUND, request);
        }
    }
}