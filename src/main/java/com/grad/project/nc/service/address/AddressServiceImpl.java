package com.grad.project.nc.service.address;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.persistence.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressDao addressDao;

    @Override
    public Address findByDomainId(Long domainId) {
        return addressDao.findDomainAddressById(domainId);
    }
}
