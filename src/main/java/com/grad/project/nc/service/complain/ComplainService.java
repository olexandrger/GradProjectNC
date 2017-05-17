package com.grad.project.nc.service.complain;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductOrder;

import java.util.Collection;

/**
 * Created by DeniG on 16.05.2017.
 */
public interface ComplainService {
    Collection<Complain> getAllComplains(long size, long offset);
    Complain newComplain(long userId, long instanceId, long reasonId, String title, String content);
}
