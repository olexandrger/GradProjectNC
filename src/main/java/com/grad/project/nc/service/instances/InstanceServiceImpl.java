package com.grad.project.nc.service.instances;

import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.persistence.ProductInstanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class InstanceServiceImpl implements InstanceService {

    private ProductInstanceDao productInstanceDao;

    @Autowired
    public InstanceServiceImpl(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    @Override
    public ProductInstance getById(Long id) {
        return productInstanceDao.find(id);
    }

    @Override
    public boolean isInstanceOwnedBy(long instanceId, long userId) {
        return productInstanceDao.find(instanceId).getDomain().getUsers().stream().anyMatch(user -> user.getUserId() == userId);
    }

    @Override
    public Collection<ProductInstance> getByDomainId(Long domainId) {
        return productInstanceDao.findByDomainId(domainId);
    }

    @Override
    public Collection<ProductInstance> getAll() {
        return productInstanceDao.findAll();
    }

    @Override
    public Collection<ProductInstance> getByUserId(Long id) {
        return productInstanceDao.findByUserId(id);
    }
}
