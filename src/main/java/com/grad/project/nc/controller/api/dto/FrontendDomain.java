package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstanceProduct;
import com.grad.project.nc.model.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendDomain {
    private Long domainId;
    private String domainName;
    private FrontendCategory domainType;
    private FrontendAddress address;
    private List<FrontendUser> users;
    //TODO instances
    private List<FrontendInstance> productInstances;

    public static FrontendDomain fromEntity(Domain domain) {
        return builder()
                .domainId(domain.getDomainId())
                .domainName(domain.getDomainName())
                .domainType(FrontendCategory.fromEntity(domain.getDomainType()))
                .address(FrontendAddress.fromEntity(domain.getAddress()))
                .users(domain.getUsers().stream().map(FrontendUser::fromEntity).collect(Collectors.toList()))
                .productInstances(domain.getProductInstances().stream().map(FrontendInstance::fromEntity).collect(Collectors.toList()))
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
//                .productInstances(getProductInstances()
//                        .stream()
//                        .map(productInstance -> productInstance.toModel()))
                .build();
    }
}
