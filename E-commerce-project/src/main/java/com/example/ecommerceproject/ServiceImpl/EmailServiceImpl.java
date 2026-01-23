package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Service.EmailService;
import com.example.ecommerceproject.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailDto emailDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDto.getTo());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getBody());
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // optional: log the error
        }
    }
}
