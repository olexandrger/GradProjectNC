package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Domain;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendDomain {
    private Long domainId;
    private String domainName;
    private Long regionId;
    private FrontendAddress address;
    private FrontendCategory domainType;

    public static FrontendDomain fromEntity(Domain domain) {
        return builder()
                .domainId(domain.getDomainId())
                .domainName(domain.getDomainName())
                .regionId(domain.getAddress().getLocation().getRegion().getRegionId())
                .address(FrontendAddress.fromEntity(domain.getAddress()))
                .domainType(FrontendCategory.fromEntity(domain.getDomainType()))
                .build();
    }
}
