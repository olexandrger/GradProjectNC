package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.complain.ComplainService;
import com.grad.project.nc.service.exceptions.IncorrectUserDataException;
import com.grad.project.nc.service.profile.ProfileService;
import com.grad.project.nc.service.security.AutoLoginService;
import com.grad.project.nc.service.security.UserService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class EditProfileController {
    @Autowired
    private AutoLoginService loginService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ComplainService complainService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public FrontUser dataTypes() {
        return mapUserToFrontUser(userService.getCurrentUser());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map update(@RequestBody FrontUser frontUser) {

        Map<String, String> response = new HashMap<>();

        User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = mapFrontUserToUser(frontUser);
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
            loginService.autologin(user.getEmail(), user.getPassword());
            response.put("message", "Your password has been changed");
        } catch (IncorrectUserDataException e) {
            response.put("message", e.getMessage());
        }
        response.put("status", profileService.getStatus());

        return response;
    }

    @RequestMapping(value = "complaint/get/all/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getComplains(@PathVariable Long size, @PathVariable Long offset){
        return complainService.getAllComplainsByUserId(userService.getCurrentUser().getUserId(), size, offset).stream()
                .map(FrontendComplain :: fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private User mapFrontUserToUser(FrontUser frontUser){
        User user = new User();

        user.setUserId(frontUser.getUserId());
        user.setFirstName(frontUser.getFirstName());
        user.setLastName(frontUser.getLastName());
        user.setEmail(frontUser.getEmail());
        user.setPassword(frontUser.getPassword());
        user.setPhoneNumber(frontUser.getPhoneNumber());
        user.setRoles(frontUser.getRoles());

        return user;
    }
    private FrontUser mapUserToFrontUser(User user){

        return FrontUser
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();

    }

    @Data
    @Builder
    private static class FrontUser{
        private Long userId;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;

        private List<Role> roles;

    }

}
