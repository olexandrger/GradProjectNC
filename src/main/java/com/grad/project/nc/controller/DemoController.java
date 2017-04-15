package com.grad.project.nc.controller;

import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.DatabaseStub;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DemoController {

    @RequestMapping(value = "/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/users")
    public String findUsers(Model model) {
        List<User> users = DatabaseStub.getUsers();
        model.addAttribute("users", users);
        return "users";
    }
}