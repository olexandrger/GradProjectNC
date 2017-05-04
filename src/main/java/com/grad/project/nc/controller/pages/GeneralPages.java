package com.grad.project.nc.controller.pages;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GeneralPages {
    @RequestMapping({"/", "/index"})
    public String root() {
        return "index";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @RequestMapping("/catalog")
    public String catalog() {
        return "catalog";
    }
}
