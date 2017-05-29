package com.grad.project.nc.configuration;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@org.springframework.context.annotation.Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
    @Value("${email.config.server.host}") String serverHost;
    @Value("${email.config.server.port}") Integer serverPort;
    @Value("${email.config.server.login}") String serverLogin;
    @Value("${email.config.server.password}") String serverPassword;

    @Bean
    public JavaMailSenderImpl createJavaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(serverHost);
        sender.setUsername(serverLogin);
        sender.setPassword(serverPassword);
        sender.setPort(serverPort);
        return sender;
    }


    @Value("${email.config.templatesPath}") String templatesPathPrefix;

    @Bean
    public Configuration createFreemarkerConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);

        TemplateLoader loader = new ClassTemplateLoader(this.getClass(), templatesPathPrefix);
        configuration.setTemplateLoader(loader);

        return configuration;
    }
}
