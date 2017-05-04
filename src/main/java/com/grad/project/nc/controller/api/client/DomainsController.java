package com.grad.project.nc.controller.api.client;


import com.grad.project.nc.model.Domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/client/domains/")
public class DomainsController {
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public Collection<DomainAddress> getUserDomains() {
        return Arrays.asList(
            new DomainAddress(1L, "Domain #1", 1L, "City1", "Street1", "13A", 14),
            new DomainAddress(2L, "Domain #3", 2L, "City 2", "Street 2", "6", 27)
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
