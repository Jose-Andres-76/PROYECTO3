package com.project.demo.services.auth;

import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.AuthAccess;
import com.project.demo.logic.entity.user.LoginResponse;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    public LoginResponse processGoogleUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String lastname = oAuth2User.getAttribute("family_name");
        String providerId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        // Check if user exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            // Update Google info if needed
            if (user.getProviderId() == null) {
                user.setProviderId(providerId);
                user.setAccess(AuthAccess.GOOGLE);
                user.setEmailVerified(emailVerified != null ? emailVerified.toString() : "true");
                if (picture != null) {
                    user.setUrlImage(picture);
                }
                userRepository.save(user);
            }
        } else {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setLastname(lastname);
            user.setProviderId(providerId);
            user.setAccess(AuthAccess.GOOGLE);
            user.setEmailVerified(emailVerified != null ? emailVerified.toString() : "true");
            user.setUrlImage(picture);
            user.setPassword(null); // No password for Google users
            user.setPoints(0);

            // Set default role
            Optional<Role> defaultRole = roleRepository.findByName(RoleEnum.FATHER);
            if (defaultRole.isPresent()) {
                user.setRole(defaultRole.get());
            }

            user = userRepository.save(user);
        }

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setAuthUser(user);

        return response;
    }

    public LoginResponse processGoogleUserFromFrontend(String email, String name, String lastname, 
                                                      String providerId, String picture) {
        // Check if user exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            // Update Google info if needed
            if (user.getProviderId() == null || !user.getProviderId().equals(providerId)) {
                user.setProviderId(providerId);
                user.setAccess(AuthAccess.GOOGLE);
                user.setEmailVerified("true");
                if (picture != null) {
                    user.setUrlImage(picture);
                }
                userRepository.save(user);
            }
        } else {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setLastname(lastname);
            user.setProviderId(providerId);
            user.setAccess(AuthAccess.GOOGLE);
            user.setEmailVerified("true");
            user.setUrlImage(picture);
            user.setPassword(null); // No password for Google users
            user.setPoints(0);

            // Set default role
            Optional<Role> defaultRole = roleRepository.findByName(RoleEnum.FATHER);
            if (defaultRole.isPresent()) {
                user.setRole(defaultRole.get());
            }

            user = userRepository.save(user);
        }

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setAuthUser(user);

        return response;
    }
} 