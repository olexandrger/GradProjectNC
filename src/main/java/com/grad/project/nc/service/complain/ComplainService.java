package com.grad.project.nc.service.complain;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductOrder;

import java.util.Collection;

/**
 * Created by DeniG on 16.05.2017.
 */
public interface ComplainService {
    public Collection<Complain> getAllComplains(long size, long offset);
    public Complain newComplain(long userId, long instanceId, long reasonId, String title, String content);
    public void appointComplain(long userId, long complainId);
    public void updadeComplainResponse(long complainId, long userId, String response);
    public void completeComplaint(long userId, long complainId);
    public Collection<Complain> findByInstanceId(Long instanceId, long size, long offset);
}
