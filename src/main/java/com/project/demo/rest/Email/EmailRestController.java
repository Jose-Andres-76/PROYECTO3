package com.project.demo.rest.Email;


import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.services.email.EmailModel;
import com.project.demo.services.email.EmailService;
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

@RestController
@RequestMapping("/email")

public class EmailRestController {
    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> getAll(
            HttpServletRequest request) {
        return new GlobalResponseHandler().handleResponse(  "LLEGUE A EMAIL: " , HttpStatus.OK, request);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FATHER')")
    public ResponseEntity<?> sendEmail(@RequestBody EmailModel email, HttpServletRequest request) {
        EmailModel sendmail = email;
        try {
            emailService.sendSimpleEmail(sendmail);
            return new GlobalResponseHandler().handleResponse(  "Email sent successfully to " + sendmail.getTo(),
                    sendmail, HttpStatus.OK, request);
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(  "Error sending email: " + sendmail.getTo(),
                    sendmail, HttpStatus.OK, request);
        }
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
