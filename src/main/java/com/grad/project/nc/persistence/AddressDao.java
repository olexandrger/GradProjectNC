package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Address;

public interface AddressDao extends CrudDao<Address> {

    Address findDomainAddressById(Long domainId);
}