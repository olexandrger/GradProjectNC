package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.data.RegistrationResponseHolder;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

//    private UserService userService;
//    private EmailService emailService;
//
//    @Autowired
//    public RegistrationController(UserService userService, EmailService emailService) {
//        this.userService = userService;
//        this.emailService = emailService;
//    }
//
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public Map register(@RequestParam("firstName") String firstName,
//                        @RequestParam("lastName") String lastName,
//                        @RequestParam("email") String email,
//                        @RequestParam("password") String password,
//                        @RequestParam("phone") String phone) {
//
//        User user = userService.createUser(firstName, lastName, email, password, phone, Collections.singletonList("CLIENT"));
//        emailService.sendRegistrationEmail(user);
//
//        return Collections.singletonMap("status", "success");
//    }

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public RegistrationResponseHolder registration(@RequestBody User user) {

        RegistrationResponseHolder registrationResponse = new RegistrationResponseHolder();

        if (!registrationService.register(user)) {
            registrationResponse.setMessageError(registrationService.getMessageError());
        } else {
            registrationResponse.setUrl("/login");
        }
        registrationResponse.setStatus(registrationService.getStatus());

        emailService.sendRegistrationEmail(user);

        return registrationResponse;
    }
}
