package com.project.demo.rest.auth;

import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.services.email.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/recovery")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryService recoveryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        recoveryService.sendRecoveryCode(email);
        return ResponseEntity.ok("Code send successfully");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        boolean valid = recoveryService.validateCode(email, code);
        return valid ? ResponseEntity.ok("Validate Code") : ResponseEntity.badRequest().body("Invalid Code");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String newPassword = body.get("newPassword");
        if (!recoveryService.validateCode(email, code)) {
            return ResponseEntity.badRequest().body("Invalid Code or Expira");
        }
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        var user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        recoveryService.deleteToken(email);
        return ResponseEntity.ok("Password Change");
    }
}
