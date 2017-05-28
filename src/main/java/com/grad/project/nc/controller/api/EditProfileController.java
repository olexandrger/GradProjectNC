package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.exceptions.IncorrectUserDataException;
import com.grad.project.nc.service.profile.ProfileService;
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
    private AutoLoginService loginService;
    @Autowired
    private ProfileService profileService;

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

        try {
            profileService.validationGeneralInformation(user);
            profileService.updateGeneralInformation(user);
            loginService.autologin(user.getEmail(), oldUser.getPassword());
            response.put("message", "Changes saved");
        } catch (IncorrectUserDataException e) {
            response.put("message", e.getMessage());
        }
        response.put("status", profileService.getStatus());

        return response;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map changePassword(@RequestParam("currentPassword") String currentPassword,
                              @RequestParam("newPassword") String newPassword) {

        Map<String, String> response = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            profileService.updatePassword(user, currentPassword, newPassword);
            response.put("message", "Your password has been changed");
        } catch (IncorrectUserDataException e) {
            response.put("message", e.getMessage());
        }
        response.put("status", profileService.getStatus());

        return response;
    }

}
