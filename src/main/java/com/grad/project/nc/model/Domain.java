package com.grad.project.nc.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Domain {
    private Long domainId;
    private String domainName;
    private Long addressId;
    private Long domainTypeId;
}
