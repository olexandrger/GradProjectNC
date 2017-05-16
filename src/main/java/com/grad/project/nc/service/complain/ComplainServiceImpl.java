package com.grad.project.nc.service.complain;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.persistence.ComplainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by DeniG on 16.05.2017.
 */
@Service
public class ComplainServiceImpl implements ComplainService {

    private ComplainDao complainDao;



    @Autowired
    public ComplainServiceImpl(ComplainDao complainDao) {
        this.complainDao = complainDao;
    }

    @Override
    public Collection<Complain> getAllComplains(long size, long offset) {
        return complainDao.findAll(size, offset);
    }
}
