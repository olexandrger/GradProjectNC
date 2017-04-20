package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${email.sender}")
    private String senderAddress;

    @Value("${email.server.host}")
    private String serverHost;
    @Value("${email.server.port}")
    private Integer serverPort;
    @Value("${email.server.login}")
    private String serverLogin;
    @Value("${email.server.password}")
    private String serverPassword;

    @PostConstruct
    private void init() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(serverHost);
        sender.setUsername(serverLogin);
        sender.setPassword(serverPassword);
        sender.setPort(serverPort);
        this.mailSender = sender;
    }

    @Override
    @Async
    public void sendRegistrationEmail(User user) {
        //TODO after ERD completion change it to user's email
        String userAddress = user.getUsername();

        logger.info("Sending email to " + userAddress);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(senderAddress);
            helper.setSubject("Welcome");
            helper.setTo(userAddress);
            helper.setText("Thank you for registering!");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        logger.info("Email sent to " + userAddress);
    }
}
