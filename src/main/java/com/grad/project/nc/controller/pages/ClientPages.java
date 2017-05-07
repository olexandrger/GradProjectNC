package com.grad.project.nc.controller.pages;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientPages {
    @RequestMapping("/client/instance")
    public String getInstancePage() {
        return "/client/instance";
    }
}
