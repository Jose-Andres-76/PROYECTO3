package com.project.demo.services.email;

import com.project.demo.logic.entity.email.PasswordResetToken;
import com.project.demo.logic.entity.email.PasswordResetTokenRepository;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordRecoveryService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    public void sendRecoveryCode(String email) {
        if (userRepository.findByEmail(email).isEmpty()) return;

        String code = String.valueOf(100000 + new Random().nextInt(900000));
        PasswordResetToken token = tokenRepository.findByEmail(email)
                .orElse(new PasswordResetToken());
        token.setEmail(email);
        token.setToken(code);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // 15 min

        tokenRepository.save(token);

        emailService.sendSimpleEmail(new EmailModel(
                email,
                "Code recover",
                "Your recovery code is the following: " + code
        ));
    }

    public boolean validateCode(String email, String code) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByEmailAndToken(email, code);
        if (tokenOpt.isEmpty()) return false;
        PasswordResetToken token = tokenOpt.get();
        return token.getExpiryDate().after(new Date());
    }

    public void deleteToken(String email) {
        tokenRepository.findByEmail(email).ifPresent(tokenRepository::delete);
    }
}