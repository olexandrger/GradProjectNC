package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Domain;

import java.util.List;

public interface DomainDao extends CrudDao<Domain> {

    List<Domain> findByUserId(Long userId);

    void deleteDomainUser(Long domainId, Long userId);

    void addDomainUser(Long domainId, Long userId);
}
