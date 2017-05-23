package com.grad.project.nc.service.profile;

import com.grad.project.nc.model.User;
import org.springframework.stereotype.Service;

public interface ProfileService {

    public boolean updateGeneralInformation(User user);

    public boolean updatePassword(User user, String currentPassword, String newPassword);

    public String getStatus();

    public String getMessage();
}
