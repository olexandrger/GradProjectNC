package com.grad.project.nc.controller;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("user") User user) {

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        user.setAuthorities(roles);
        emailService.sendRegistrationEmail(user);
        userService.createUser(user);

        return "redirect:/index";
    }
}
