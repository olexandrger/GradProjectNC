package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Domain {
    private Long domainId;
    private String domainName;
    private Address address;
    private Category domainType;

    private List<User> users;
    private List<ProductInstance> productInstances;
}
