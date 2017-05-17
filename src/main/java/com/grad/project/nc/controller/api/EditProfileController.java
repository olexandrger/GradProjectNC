package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.security.AutoLoginService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class EditProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private AutoLoginService loginService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User dataTypes() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map update(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.setUserId(oldUser.getUserId());

        if (!userService.updateGeneralInformation(user)) {
            response.put("status", "error");
            response.put("message", "Error :(");
        } else {
            loginService.autologin(user.getEmail(), oldUser.getPassword());
            response.put("status", "success");
            response.put("message", "Changes saved");
        }
        return response;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map changePassword(@RequestParam("currentPassword") String currentPassword,
                              @RequestParam("newPassword") String newPassword) {

        Map<String, String> response = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!encoder.matches(currentPassword, user.getPassword())) {
            response.put("status", "error");
            response.put("message", "Invalid current password.");
        } else if (!isPasswordValid(newPassword)) {
            response.put("status", "error");
            response.put("message", "Incorrect new password.");
        } else {
            user.setPassword(newPassword);
            if (!userService.updatePassword(user)) {
                response.put("status", "error");
                response.put("message", "Error.");
            } else {
                loginService.autologin(user.getEmail(), user.getPassword());
                response.put("status", "success");
                response.put("message", "Your password has been changed.");
            }
        }

        return response;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8 ||
                password.length() > 20) {
            return false;
        }
        final String regex = "^[a-zA-Z0-9!@#$%^&*()_+|~\\-=\\/‘\\{\\}\\[\\]:\";’<>?,./]+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

}
