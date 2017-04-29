package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.User;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@RestController
public class RegistrationController {

    private UserService userService;
    private EmailService emailService;

    @Autowired
    public RegistrationController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map register(@RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName,
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam("phone") String phone) {

        User user = userService.createUser(firstName, lastName, email, password, phone, Collections.singletonList("CLIENT"));
        emailService.sendRegistrationEmail(user);

        return Collections.singletonMap("status", "success");
    }
}
