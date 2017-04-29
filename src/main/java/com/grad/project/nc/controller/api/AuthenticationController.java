package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.data.ResponseHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AuthenticationController {
    @RequestMapping("/login/success")
    public String loginSuccess() {
        return "redirect:/api/name";
    }

    @RequestMapping("/login/failed")
    @ResponseBody
    public ResponseHolder loginFailed() {
        ResponseHolder response = new ResponseHolder();
        response.setStatus("failed");
        return response;
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ResponseHolder logoutSuccess() {
        ResponseHolder response = new ResponseHolder();
        response.setStatus("success");
        return response;
    }
}
