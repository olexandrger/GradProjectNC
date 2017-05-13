package com.grad.project.nc.controller.api.client;


import com.grad.project.nc.controller.api.data.RegistrationResponseHolder;
import com.grad.project.nc.controller.api.dto.FrontendAddress;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.DomainDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/domains/")
public class DomainsController {

    @Autowired
    private DomainDao domainDao;

    //TODO rework after base domains done
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public Collection<FrontendDomain> getUserDomains() {
        return domainDao.findByUserId(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId())
                .stream().map(FrontendDomain::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendDomain getDomain(@PathVariable Long id) {
        return FrontendDomain.fromEntity(domainDao.find(id));
    }

    @RequestMapping(path = "/getDomainsByAddress", method = RequestMethod.GET)
    @ResponseBody
    public List<FrontendDomain> getDomainsByAddress(@RequestParam("address") String address,@RequestParam("aptNum") String aptNum) {

        //TODO rework after domains will be finished
        List<FrontendDomain> list = new ArrayList<>();
        list.add(FrontendDomain.builder().domainId((long) 1).domainName("Mock domain for: "+ address + aptNum). build());


        return list;
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
