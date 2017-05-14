package com.grad.project.nc.controller.api.client;


import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.domains.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/domains/")
public class DomainsController {

    @Autowired
    private DomainService domainService;

    //TODO rework after base domains done
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public Collection<FrontendDomain> getUserDomains() {
        return domainService.findByUserId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId())
                .stream().map(FrontendDomain::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendDomain getDomain(@PathVariable Long id) {
        return FrontendDomain.fromEntity(domainService.find(id));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, String> deleteDomain(@RequestBody Map<String, Long> domainId) {
        Map<String, String> result = new HashMap<>();


        result.put("status", "success");
        result.put("message", "Domain deleted successfully");
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Map<String, String> updateDomain(@RequestBody FrontendDomain frontendDomain) {
        Map<String, String> result = new HashMap<>();
        try {
            Domain domain = domainService.convertFrontendDomainToDomain(frontendDomain);
            if (domainService.find(frontendDomain.getDomainId()) == null) {
                domainService.add(domain);
                result.put("status", "success");
                result.put("message", "Domain added succesfully");
            } else {
                domainService.update(domain);
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



//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private static class DomainAddress {
//        private Long domainId;
//        private String domainName;
//        private Long regionId;
//        private String city;
//        private String street;
//        private String building;
//        private Integer apartment;
//    }
}
