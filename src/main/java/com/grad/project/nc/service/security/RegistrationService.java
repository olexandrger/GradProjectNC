package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegistrationService {
    private final String INVALID_EMAIL = "Invalid email address";
    private final String EMAIL_ALREADY_EXISTS = "Email already exists";

    private final String SUCCESS = "success";
    private final String ERROR = "error";

    private String status;
    private String messageError;

    @Autowired
    private UserService userService;

    public boolean register(User user) {
        if (!isEmailValid(user.getEmail())) {
            status = ERROR;
            messageError = INVALID_EMAIL;
            return false;
        }
        try {
            userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), Collections.singletonList("CLIENT"));
        } catch (DuplicateKeyException e) {
            status = ERROR;
            messageError = EMAIL_ALREADY_EXISTS;
            return false;
        }
        status = SUCCESS;
        return true;
    }

    private boolean isEmailValid(String email) {
        final String regex = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`\\{|\\}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
