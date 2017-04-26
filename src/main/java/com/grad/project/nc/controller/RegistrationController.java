package com.grad.project.nc.controller;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.UserOLD;
import com.grad.project.nc.persistence.ProductDao;
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
        model.addAttribute("user", new UserOLD());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("user") UserOLD userOLD) {

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        userOLD.setAuthorities(roles);
        emailService.sendRegistrationEmail(userOLD);
        userService.createUser(userOLD);


        return "redirect:/index";
    }
}
