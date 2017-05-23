package com.grad.project.nc.service.profile;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.security.AutoLoginService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AutoLoginService loginService;

    private final String INVALID_EMAIL = "Incorrect email address";
    private final String INCORRECT_NEW_PASSWORD = "Incorrect new password. Minimum length - 8 symbols";
    private final String INVALID_CURRENT_PASSWORD = "Invalid current password";
    private final String INCORRECT_PHONE = "Incorrect phone number";
    private final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private final String FIRST_NAME_IS_EMPTY = "First name is empty";
    private final String LAST_NAME_IS_EMPTY = "Last name is empty";
    private final String EMAIL_IS_EMPTY = "Email is empty";
    private final String PHONE_IS_EMPTY = "Phone is empty";
    private final String CHANGES_SAVED = "Changes saved";
    private final String PASSWORD_SAVED = "Your password has been changed";

    private final String SUCCESS = "success";
    private final String ERROR = "error";

    private String status;
    private String message;

    @Autowired
    private UserService userService;

    @Override
    public boolean updateGeneralInformation(User user) {
        if (user.getFirstName().isEmpty()) {
            status = ERROR;
            message = FIRST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getLastName().isEmpty()) {
            status = ERROR;
            message = LAST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getEmail().isEmpty()) {
            status = ERROR;
            message = EMAIL_IS_EMPTY;
            return false;
        }
        if (user.getPhoneNumber().isEmpty()) {
            status = ERROR;
            message = PHONE_IS_EMPTY;
            return false;
        }
        if (!isPhoneNumberValid(user.getPhoneNumber())) {
            status = ERROR;
            message = INCORRECT_PHONE;
            return false;
        }
        if (!isEmailValid(user.getEmail())) {
            status = ERROR;
            message = INVALID_EMAIL;
            return false;
        }
        if (!userService.updateGeneralInformation(user)) {
            status = ERROR;
            message = EMAIL_ALREADY_EXISTS;
            return false;
        }
        status = SUCCESS;
        message = CHANGES_SAVED;
        return true;
    }

    @Override
    public boolean updatePassword(User user, String currentPassword, String newPassword) {
        if (!encoder.matches(currentPassword, user.getPassword())) {
            status = ERROR;
            message = INVALID_CURRENT_PASSWORD;
            return false;
        }
        if (!isPasswordValid(newPassword)) {
            status = ERROR;
            message = INCORRECT_NEW_PASSWORD;
            return false;
        }
        user.setPassword(newPassword);

        if (!userService.updatePassword(user)) {
            status = ERROR;
            message = ERROR;
        }
        loginService.autologin(user.getEmail(), user.getPassword());
        status = SUCCESS;
        message = PASSWORD_SAVED;
        return true;
    }

    boolean isEmailValid(String email) {
        final String regex = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`\\{|\\}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    boolean isPhoneNumberValid(String phone) {
        final String regex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(phone);
        return m.matches();
    }

    boolean isPasswordValid(String password) {
        if (password.length() < 8 ||
                password.length() > 20) {
            return false;
        }
        final String regex = "^[a-zA-Z0-9!@#$%^&*()_+|~\\-=\\/‘\\{\\}\\[\\]:\";’<>?,./]+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
