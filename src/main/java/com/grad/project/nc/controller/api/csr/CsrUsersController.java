package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendUser;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

/**
 * Created by DeniG on 10.05.2017.
 */

@RestController
@RequestMapping("/api/csr/users")
public class CsrUsersController {

    private UserService userService;

    @Autowired
    public CsrUsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/find/{mail}", method = RequestMethod.GET)
    FrontendUser findUserByEmail(@PathVariable String mail){
        return FrontendUser.fromEntity(userService.findByEMail(mail));
    }
}
