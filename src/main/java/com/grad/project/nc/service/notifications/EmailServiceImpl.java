package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    private Configuration freemarkerConfiguration;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, @Qualifier("createFreemarkerConfiguration") Configuration configuration) {
        this.mailSender = mailSender;
        this.freemarkerConfiguration = configuration;
    }

    private void sendEmail(Mail mail) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(mail.getAddress());
            helper.setFrom(mail.getContent().getSender());
            helper.setSubject(mail.getContent().getSubject());

            Template template = freemarkerConfiguration.getTemplate(mail.getContent().getTemplateName());
            String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getContent().getContext());
            helper.setText(body);

            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            logger.error("Can not send email", e);
        }
    }

    @Override
    @Async
    public void sendRegistrationEmail(User user) {
        logger.info("Sending registration email to " + user.getUsername());
        sendEmail(new Mail(user.getEmail(), new RegistrationMailContent(user)));
    }

    @Override
    @Async
    public void sendNewOrderEmail(ProductOrder order) {
        User user = order.getUser();
        logger.info("Sending new order email to " + user.getUsername());
        sendEmail(new Mail(user.getEmail(), new NewOrderMailContent(order)));
    }

    @Override
    @Async
    public void sendInstanceStatusChangedEmail(ProductInstance instance) {
        logger.info("Sending instance mails");
        instance.getDomain().getUsers().forEach((user) -> {
            sendEmail(new Mail(user.getEmail(), new InstanceStatusChangedMailContent(user, instance)));
        });
    }

    @Override
    public void sendNewComplaintEmail(Complain complain) {
        User user = complain.getUser();
        logger.info("Sending new complain email to " + user.getUsername());
        sendEmail(new Mail(user.getEmail(), new NewComplaintMailContent(complain)));
    }

    @Override
    public void sendComplaintUnderConsiderationChangedEmail(Complain complain) {
        User user = complain.getUser();
        logger.info("Sending complain under considerations email to " + user.getUsername());
        sendEmail(new Mail(user.getEmail(), new ComplaintUnderConsiderationMailContent(complain)));
    }

    @Override
    public void sendComplaintCompleteEmail(Complain complain) {
        User user = complain.getUser();
        logger.info("Sending complain complete email to " + user.getUsername());
        sendEmail(new Mail(user.getEmail(), new ComplaintCompletedMailContent(complain)));
    }


    @Data
    @AllArgsConstructor
    private static class Mail {
        private String address;
        private MailContent content;
    }
}