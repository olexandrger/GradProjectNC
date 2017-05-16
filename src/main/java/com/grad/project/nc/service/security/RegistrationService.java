package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final String INVALID_EMAIL = "Incorrect email address";
    private final String INCORRECT_PASSWORD = "Incorrect password";
    private final String INCORRECT_PHONE = "Incorrect phone number";
    private final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private final String FIRST_NAME_IS_EMPTY = "First name is empty";
    private final String LAST_NAME_IS_EMPTY = "Last name is empty";
    private final String EMAIL_IS_EMPTY = "Email is empty";
    private final String PASSWORD_IS_EMPTY = "Password is empty";
    private final String PHONE_IS_EMPTY = "Phone is empty";

    private final String SUCCESS = "success";
    private final String ERROR = "error";

    private String status;
    private String messageError;

    @Autowired
    private UserService userService;

    public boolean register(User user) {
        List<String> roles ;
        if(user.getRoles() == null || user.getRoles().isEmpty()){
            roles = new ArrayList<>();
            roles.add("ROLE_CLIENT");
        }else {
            roles = user.getRoles().stream().map(x -> x.getRoleName()).collect(Collectors.toList());
        }


        if (user.getFirstName().isEmpty()) {
            status = ERROR;
            messageError = FIRST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getLastName().isEmpty()) {
            status = ERROR;
            messageError = LAST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getEmail().isEmpty()) {
            status = ERROR;
            messageError = EMAIL_IS_EMPTY;
            return false;
        }
        if (user.getPassword().isEmpty()) {
            status = ERROR;
            messageError = PASSWORD_IS_EMPTY;
            return false;
        }
        if (!isPasswordValid(user.getPassword())) {
            status = ERROR;
            messageError = INCORRECT_PASSWORD;
            return false;
        }
        if (user.getPhoneNumber().isEmpty()) {
            status = ERROR;
            messageError = PHONE_IS_EMPTY;
            return false;
        }
        if (!isPhoneNumberValid(user.getPhoneNumber())) {
            status = ERROR;
            messageError = INCORRECT_PHONE;
            return false;
        }
        if (!isEmailValid(user.getEmail())) {
            status = ERROR;
            messageError = INVALID_EMAIL;
            return false;
        }
        try {
            userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), roles/*Collections.singletonList("ROLE_CLIENT")*/);
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

    private boolean isPasswordValid(String password) {
        if (password.length() < 8 || password.length() > 20)
            return false;
        final String regex = "^[a-zA-Z0-9!@#$%^&*()_+|~\\-=\\/‘\\{\\}\\[\\]:\";’<>?,./]+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    private boolean isPhoneNumberValid(String phone) {
        final String regex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(phone);
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
