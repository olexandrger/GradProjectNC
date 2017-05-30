package com.grad.project.nc.service.profile;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.exceptions.IncorrectUserDataException;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final String INVALID_EMAIL = "Incorrect email address";
    private final String INCORRECT_NEW_PASSWORD = "Incorrect new password. Minimum length - 8 symbols";
    private final String INVALID_CURRENT_PASSWORD = "Invalid current password";
    private final String INCORRECT_PHONE = "Incorrect phone number";
    private final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private final String FIRST_NAME_IS_EMPTY = "First name is empty";
    private final String LAST_NAME_IS_EMPTY = "Last name is empty";
    private final String EMAIL_IS_EMPTY = "Email is empty";
    private final String PHONE_IS_EMPTY = "Phone is empty";

    private final String SUCCESS = "success";
    private final String ERROR = "error";

    private String status;

    @Autowired
    private UserService userService;

    @Override
    public User updateGeneralInformation(User user) throws IncorrectUserDataException {
        try {
            userService.updateGeneralInformation(user);
        } catch (DuplicateKeyException e) {
            throw new IncorrectUserDataException(EMAIL_ALREADY_EXISTS);
        }
        status = SUCCESS;
        return user;
    }

    @Override
    public User updatePassword(User user, String currentPassword, String newPassword) throws IncorrectUserDataException {
        status = ERROR;
        if (!encoder.matches(currentPassword, user.getPassword()))
            throw new IncorrectUserDataException(INVALID_CURRENT_PASSWORD);

        if (!isPasswordValid(newPassword))
            throw new IncorrectUserDataException(INCORRECT_NEW_PASSWORD);

        user.setPassword(newPassword);

        userService.updatePassword(user);

        status = SUCCESS;
        return user;
    }

    public void validationGeneralInformation(User user) throws IncorrectUserDataException {
        status = ERROR;
        if (user.getFirstName().isEmpty()) {
            throw new IncorrectUserDataException(FIRST_NAME_IS_EMPTY);
        }
        if (user.getLastName().isEmpty()) {
            throw new IncorrectUserDataException(LAST_NAME_IS_EMPTY);
        }
        if (user.getEmail().isEmpty()) {
            throw new IncorrectUserDataException(EMAIL_IS_EMPTY);
        }
        if (user.getPhoneNumber().isEmpty()) {
            throw new IncorrectUserDataException(PHONE_IS_EMPTY);
        }
        if (!isPhoneNumberValid(user.getPhoneNumber())) {
            throw new IncorrectUserDataException(INCORRECT_PHONE);
        }
        if (!isEmailValid(user.getEmail())) {
            throw new IncorrectUserDataException(INVALID_EMAIL);
        }
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

}
