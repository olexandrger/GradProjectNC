package com.grad.project.nc.service.notifications;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

abstract class MailContent {
    @Getter @Setter
    private String subject;

    @Getter @Setter
    private String sender;

    @Getter @Setter
    private String templateName;

    @Getter
    protected Map<String, Object> context = new HashMap<>();
}
