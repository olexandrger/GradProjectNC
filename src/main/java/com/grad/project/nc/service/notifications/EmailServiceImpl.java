package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.User;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@Service
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    private Configuration freemarkerConfiguration;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${email.sender}")
    private String senderAddress;

    @Value("${email.templates.prefix}")
    private String templatesPathPrefix;

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
        initMailSender();
        initFreemarkerConfiguration();
    }

    private void initMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(serverHost);
        sender.setUsername(serverLogin);
        sender.setPassword(serverPassword);
        sender.setPort(serverPort);
        this.mailSender = sender;
    }

    private void initFreemarkerConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);

        TemplateLoader loader = new ClassTemplateLoader(this.getClass(), templatesPathPrefix);
        configuration.setTemplateLoader(loader);

        this.freemarkerConfiguration = configuration;
    }

    private void sendEmail(String address, String sender, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(address);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Can not send email", e);
        }
    }

    private void sendTemplateEmail(User user, Template template, String sender, String subject) {
        //TODO after ERD completion change it to user's email
        String userAddress = user.getUsername();

        try {
            Map<String, Object> context = new HashMap<>();
            context.put("user", user);

            String message = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);

            sendEmail(userAddress, sender, subject, message);
        } catch (TemplateException | IOException e) {
            logger.error("Can not process email template", e);
        }
    }

    private Template createTemplateFromString(String templateString) {
        //Generate template cache name so they can be used again
        //return new Template("name", new StringReader(templateString), );
        return null;
    }

    private Template getTemplateFromResources(String templateName) throws IOException {
        return freemarkerConfiguration.getTemplate(templateName + ".ftl");
    }

    @Override
    @Async
    public void sendRegistrationEmail(User user) {
        logger.info("Sending registration email to " + user.getUsername());

        try {
            Template mailTemplate = getTemplateFromResources("registration");
            sendTemplateEmail(user, mailTemplate, senderAddress, "Welcome");
        } catch (IOException e) {
            logger.error("Can not load email template", e);
        }

        logger.info("Registration email sent to " + user.getUsername());
    }
}
