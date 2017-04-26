package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.User;

public interface EmailService {
    void sendRegistrationEmail(User user);
}
