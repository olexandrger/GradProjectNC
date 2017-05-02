package com.grad.project.nc.controller.api;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuthenticationController {

    @RequestMapping("/login/success")
    public Map loginSuccess() {
        return Collections.singletonMap("status", "success");
    }

    @RequestMapping("/login/failed")
    @ResponseBody
    public Map loginFailed() {
        return Collections.singletonMap("status", "failed");
    }

    @RequestMapping("/login/logout")
    @ResponseBody
    public Map logoutSuccess() {
        return Collections.singletonMap("status", "success");
    }

}
