package com.imanzi.marketplace.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(String email,String subject, String message) {
        try {
            sendHtmlEmail(email, subject, message);
            System.out.println("Email sent successfully to " + email);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            // Handle the failure accordingly, perhaps retrying or logging the error
            return false;
        }
    }

    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email to " + to + ": " + e.getMessage());
        }
        return false;
    }


}
