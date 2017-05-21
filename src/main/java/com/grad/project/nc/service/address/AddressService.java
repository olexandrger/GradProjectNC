package com.grad.project.nc.service.address;

import com.grad.project.nc.model.Address;

public interface AddressService {
    Address findByDomainId(Long domainId);
}
