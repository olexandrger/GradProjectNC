package com.grad.project.nc.service.notifications;


import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.instances.InstanceService;

public class InstanceStatusChangedMailContent extends MailContent {
    InstanceStatusChangedMailContent(User user, ProductInstance instance) {
        getContext().put("instance", instance);
        getContext().put("user", user);

        setSender("support@ncgrad.herokuapp.com");
        setSubject("Instance status changed");

        setTemplateName("instanceStatusChange.ftl");
    }
}
