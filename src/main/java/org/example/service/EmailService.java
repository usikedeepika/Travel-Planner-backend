package org.example.service;



import lombok.RequiredArgsConstructor;
import org.example.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(EmailRequest emailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getMessage());
            message.setFrom(fromEmail);

            if (emailRequest.getCc() != null && !emailRequest.getCc().trim().isEmpty()) {
                message.setCc(emailRequest.getCc());
            }
            if (emailRequest.getBcc() != null && !emailRequest.getBcc().trim().isEmpty()) {
                message.setBcc(emailRequest.getBcc());
            }

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
