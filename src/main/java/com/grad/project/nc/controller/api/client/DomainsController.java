package com.grad.project.nc.controller.api.client;


import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.DomainDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/domains/")
public class DomainsController {

    //TODO rework this class after

    @Autowired
    private DomainDao domainDao;

    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public Collection<DomainAddress> getUserDomains() {
        return domainDao.findByUserId(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId()).stream().map(item -> new DomainAddress(
                item.getDomainId(),
                item.getDomainName(),
                item.getAddress().getLocation().getRegion().getRegionId(),
                "City",
                "Street",
                "Building",
                12
                )).collect(Collectors.toList());
    }

    @RequestMapping(path = "/get/byId/{id}", method = RequestMethod.GET)
    public DomainAddress getDomain(@PathVariable Long id) {
        Domain item = domainDao.find(id);

        return new DomainAddress(
                item.getDomainId(),
                item.getDomainName(),
                item.getAddress().getLocation().getRegion().getRegionId(),
                "City",
                "Street",
                "Building",
                12
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DomainAddress {
        private Long domainId;
        private String domainName;
        private Long regionId;
        private String city;
        private String street;
        private String building;
        private Integer apartment;
    }
}
