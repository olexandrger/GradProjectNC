package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.User;

import java.io.IOException;

class RegistrationMailContent extends MailContent {

    RegistrationMailContent(User user) {
        context.put("user", user);

        setSender("support@ncgrad.herokuapp.com");
        setSubject("Welcome");

        setTemplateName("registration.ftl");
    }
}
