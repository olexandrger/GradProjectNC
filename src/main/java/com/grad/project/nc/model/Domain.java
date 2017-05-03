package com.grad.project.nc.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class Domain {
    private Long domainId;
    private String domainName;
    private Address address;
    private CategoryType domainType;
    private Collection<User> users;
}
