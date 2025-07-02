package com.project.demo.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static java.awt.SystemColor.text;

@Service

public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(EmailModel emailModel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ecoasistente53@gmail.com");
        message.setTo(emailModel.getTo());
        message.setSubject(emailModel.getSubject());
        message.setText(emailModel.getText());
        mailSender.send(message);
    }
}
