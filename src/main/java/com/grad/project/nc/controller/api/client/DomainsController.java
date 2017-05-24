package com.grad.project.nc.controller.api.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.domains.DomainService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/domains")
public class DomainsController {

    private DomainService domainService;
    private UserService userService;

    @Autowired
    public DomainsController(DomainService domainService, UserService userService) {
        this.domainService = domainService;
        this.userService = userService;
    }

    //TODO rework after base domains done
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public Collection<FrontendDomain> getUserDomains() {
        return domainService.findByUserId(userService.getCurrentUser().getUserId()).stream().map(FrontendDomain::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendDomain getDomain(@PathVariable Long id) {
        return FrontendDomain.fromEntity(domainService.find(id));
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public Map<String, String> deleteDomain(@RequestBody Map<String, Long> domainId) {
        Map<String, String> result = new HashMap<>();
        //System.out.println(domainId.get("id"));
        try {
            Domain domain = domainService.find(domainId.get("id"));
            if (domain != null) {
                domainService.delete(domain);
                result.put("status", "success");
                result.put("message", "Domain deleted succesfully");
            } else {
                result.put("status", "success");
                result.put("message", "There is no domains with this id");
            }
            return result;
        } catch (DataAccessException exception) {
            result.put("status", "error");
            result.put("message", "Can not delete domain from database");
            return result;
        }
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public Map<String, String> updateDomain(@RequestBody FrontendDomain frontendDomain) {
        Map<String, String> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(frontendDomain));
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            System.out.println("frontend error");
        }
        try {
            Domain domain = domainService.convertFrontendDomainToDomain(frontendDomain);
            System.out.println(domain.getDomainType().getCategoryId());
            if (domainService.find(frontendDomain.getDomainId()) == null) {
                domainService.add(domain);
                result.put("status", "success");
                result.put("message", "Domain added succesfully");
                result.put("domainId", domain.getDomainId().toString());
            } else {
                domainService.update(domain);
                System.out.println("in controller");
                result.put("status", "success");
                result.put("message", "Domain updated succesfully");
            }
            return result;
        } catch (DataAccessException exception) {
            result.put("status", "error");
            result.put("message", "Can not add domain to database");
            return result;
        }
    }

    @RequestMapping(path = "/get/user", method = RequestMethod.GET)
    public User getUserByEmail(@RequestParam(name = "email") String email) {
        try {
            return userService.findByEMail(email);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @RequestMapping(path = "/get/user/authorized", method = RequestMethod.GET)
    public User getAuthorizedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
