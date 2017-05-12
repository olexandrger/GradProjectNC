package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.*;
import com.grad.project.nc.persistence.DomainDao;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 10.05.2017.
 */
@RestController
@RequestMapping("/api/csr/domains")
public class CsrDomainContoller {

    //TODO services
    @Autowired
    DomainDao domainDao;

    UserService userService;

    @Autowired
    public CsrDomainContoller(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/find/{email}", method = RequestMethod.GET)
    Collection<FrontendDomain> findByUserEMail(@PathVariable String email){
        return domainDao.findByUserId(userService.findByEMail(email).getUserId())
                .stream()
                .map(FrontendDomain::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
