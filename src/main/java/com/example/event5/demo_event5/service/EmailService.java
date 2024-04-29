package com.example.event5.demo_event5.service;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void sendEmail1(String to, String subject, String text,MultipartFile[] attachments )throws MessagingException, IOException {
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        for (MultipartFile attachment : attachments) {
            DataSource dataSource = new ByteArrayDataSource(attachment.getInputStream(), attachment.getContentType());
            helper.addAttachment(attachment.getOriginalFilename(), dataSource);
        }
        emailSender.send(message);

    }

    public void sendEmail2(String email, String subject, String text) throws MessagingException {
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);
        emailSender.send(message);

    }
}
