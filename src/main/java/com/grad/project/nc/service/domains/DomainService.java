package com.grad.project.nc.service.domains;

import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.Domain;

import java.util.List;

public interface DomainService {
    Domain find(Long id);

    List<Domain> findByUserId(Long id);

    void add(Domain domain);

    void update(Domain domain);

    void delete(Domain domain);

    Domain convertFrontendDomainToDomain(FrontendDomain frontendDomain);
}
