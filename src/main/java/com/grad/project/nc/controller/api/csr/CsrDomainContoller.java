package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.domains.DomainService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 10.05.2017.
 */
@RestController
@RequestMapping("/api/csr/domains")
public class CsrDomainContoller {

    DomainService domainService;

    UserService userService;
    @Autowired
    public CsrDomainContoller(DomainService domainService, UserService userService) {
        this.domainService = domainService;
        this.userService = userService;
    }

    @RequestMapping(value = "/find/{email}", method = RequestMethod.GET)
    Map<String, Object> findByUserEMail(@PathVariable String email) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.findByEMail(email);
        if (user != null) {
            result.put("status", "found");
            result.put("userId", user.getUserId());
            result.put("domains",
                    domainService.getAllDomains(user.getUserId())
                            .stream()
                            .map(FrontendDomain::fromEntity)
                            .collect(Collectors.toCollection(ArrayList::new)));
        } else {
            result.put("status", "not found");
        }

        return result;

    }

}
