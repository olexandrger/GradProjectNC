package com.grad.project.nc.service.profile;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.exceptions.IncorrectUserDataException;
import org.springframework.stereotype.Service;

public interface ProfileService {

    public User updateGeneralInformation(User user) throws IncorrectUserDataException;

    public User updatePassword(User user, String currentPassword, String newPassword) throws IncorrectUserDataException;

    public void validationGeneralInformation(User user) throws IncorrectUserDataException;

    public String getStatus();
}
