package com.grad.project.nc.controller.pages;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientPages {
    @RequestMapping("/instance/*")
    public String getInstancePage() {
        return "/client/instance";
    }

    @RequestMapping("/domains")
    public String getDomainsPage(){
        return "client/domain";
    }
}
