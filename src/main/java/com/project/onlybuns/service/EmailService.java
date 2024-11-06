package com.project.onlybuns.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }



    public void sendActivationEmail(String to, String token) throws MessagingException {
        String subject = "Activate your account";
        String confirmationUrl = "http://localhost:8080/activate?token=" + token;
        String body = "<html><body><p>Please click the following link to activate your account: <a href=\""
                + confirmationUrl + "\">Activate</a></p></body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true za HTML

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true znači da je HTML format

        mailSender.send(message);
    }
}
