package com.project.demo.rest.auth;


import com.project.demo.logic.entity.email.EmailModel;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.services.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping("/auth/generalInfo")
public class emailGeneralInfo {

    @Autowired
    private EmailService emailService;


    @PostMapping
    public ResponseEntity<?> sendEmailInfoGeneral(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String email = body.get("email");
        EmailModel sendmail = new EmailModel();
        try {
            sendmail.setSubject("Información General de Eco Asistente AI");
            sendmail.setText("Somos una aplicación dedicada a la educación del reciclaje, fomentando el conocimiento y el crecimiento familiar.\n" +
                    "Nuestro asistente EcoAsitenteIA, nos permite colaborar con el medio ambiente y los ciudadanos, para poder identificar, clasificar y  recomendar sobre el manejo de residuos.");
            sendmail.setTo(email);
            emailService.sendSimpleEmail(sendmail);
            return new GlobalResponseHandler().handleResponse("Email sent successfully to " + sendmail.getTo(), sendmail, HttpStatus.OK, request);
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse("Error sending email: " + sendmail.getTo(), sendmail, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

}
