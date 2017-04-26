package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.UserOLD;

public interface EmailService {
    void sendRegistrationEmail(UserOLD userOLD);
}
