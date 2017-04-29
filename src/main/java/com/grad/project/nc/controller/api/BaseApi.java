package com.grad.project.nc.controller.api;

import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class BaseApi {
    @RequestMapping("/test/{data}")
    @Secured({"ADMIN", "CLIENT"})
    public String test(@PathVariable("data") String data) {
        return "Here is your data: " + data;
    }

    @RequestMapping("/secured/{data}")
    @Secured({"ADMIN"})
    public String test2(@PathVariable("data") String data) {
        return "Here is your secured data: " + data;
    }

    @RequestMapping("/name")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.isAuthenticated();
        return auth.isAuthenticated() + " " + auth.getName(); //get logged in username
    }
}
