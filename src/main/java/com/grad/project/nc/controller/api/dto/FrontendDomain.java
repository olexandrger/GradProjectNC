package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendDomain {
    private Long domainId;
    private String domainName;
    private Long regionId;
    private FrontendAddress address;
    private FrontendCategory domainType;
    private List<FrontendUser> users;

    public static FrontendDomain fromEntity(Domain domain) {
        return builder()
                .domainId(domain.getDomainId())
                .domainName(domain.getDomainName())
                .regionId(domain.getAddress().getLocation().getRegion().getRegionId())
                .address(FrontendAddress.fromEntity(domain.getAddress()))
                .domainType(FrontendCategory.fromEntity(domain.getDomainType()))
                .users(domain.getUsers().stream().map(FrontendUser::fromEntity).collect(Collectors.toList()))
                .build();
    }

    public Domain toModel(){
        return Domain.builder()
                .domainId(domainId)
                .domainName(domainName)
                .domainType(domainType.toModel())
                .address(address.toModel())
                .users(getUsers()
                        .stream()
                        .map(user -> user.toModel())
                        .collect(Collectors.toList()))
                //.productInstances()
                .build();
    }
}
