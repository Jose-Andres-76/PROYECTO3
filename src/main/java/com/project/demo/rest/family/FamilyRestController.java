package com.project.demo.rest.family;


import com.project.demo.logic.entity.family.Family;
import com.project.demo.logic.entity.family.FamilyRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
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
@RequestMapping("/families")
public class FamilyRestController {
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllFamilies(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Family> families = familyRepository.findAll(pageable).getContent();
        int totalElements = (int) familyRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(totalPages);
            meta.setTotalElements(totalElements);
            meta.setPageNumber(page);
            meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Families listed.", families, HttpStatus.OK, meta);
        }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFamilyById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Family> family = familyRepository.findById(id);
        if (family.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Family found.", family.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Family with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    //we add a method to get the multiple families of the authenticated user
    @GetMapping("/my-family/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFamilyByUserId(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Family> families = familyRepository.findByFatherId(user.get());
            if (!families.isEmpty()) {
                return new GlobalResponseHandler().handleResponse("Families found for user with id: " + userId, families, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("No families found for user with id: " + userId, HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("User with id: " + userId + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> createFamily(@RequestBody Family newFamily, HttpServletRequest request) {
        System.out.println(newFamily);


        System.out.println(newFamily.getFather().getId());
        Optional<User> father = userRepository.findById(newFamily.getFather().getId());
        User fatherFound= father.get();
        System.out.println(fatherFound);

        Optional<User> son = userRepository.findById(newFamily.getSon().getId());
        User sonFound= son.get();
        System.out.println(sonFound);

        newFamily.setFather(fatherFound);
        newFamily.setSon(sonFound);

        System.out.println(newFamily);
        Family savedFamily = familyRepository.save(newFamily);
        return new GlobalResponseHandler().handleResponse("Family created successfully.", savedFamily, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> updateFamily(@PathVariable Long id, @RequestBody Family family, HttpServletRequest request) {
        Optional<Family> existingFamilyOpt = familyRepository.findById(id);
        if (existingFamilyOpt.isPresent()) {
            Family existingFamily = existingFamilyOpt.get();
            if (family.getFather() != null) {
                existingFamily.setFather(family.getFather());
            }
            if (family.getSon() != null) {
                existingFamily.setSon(family.getSon());
            }
            Family updatedFamily = familyRepository.save(existingFamily);
            return new GlobalResponseHandler().handleResponse("Family updated successfully.", updatedFamily, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Family with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> deleteFamily(@PathVariable Long id, HttpServletRequest request) {
        Optional<Family> existingFamilyOpt = familyRepository.findById(id);
        if (existingFamilyOpt.isPresent()) {
            familyRepository.delete(existingFamilyOpt.get());
            return new GlobalResponseHandler().handleResponse("Family deleted successfully.", HttpStatus.NO_CONTENT, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Family with id: " + id + " not found.", HttpStatus.NOT_FOUND, request);
        }
    }


}
