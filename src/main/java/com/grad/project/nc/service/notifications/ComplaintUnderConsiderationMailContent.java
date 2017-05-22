package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.Complain;

/**
 * Created by DeniG on 22.05.2017.
 */
public class ComplaintUnderConsiderationMailContent extends MailContent {
    public ComplaintUnderConsiderationMailContent(Complain complain) {
        getContext().put("complaint", complain);
        getContext().put("user", complain.getUser());

        setSender("support@ncgrad.herokuapp.com");
        setSubject("Complaint under consideration");

        setTemplateName("complaintUnderConsideration.ftl");
    }
}
