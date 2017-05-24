package com.grad.project.nc.service.complain;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.service.exceptions.*;

import java.util.Collection;

/**
 * Created by DeniG on 16.05.2017.
 */
public interface ComplainService {
    public Collection<Complain> getAllComplains(long size, long offset);
    public Complain newComplain(long userId, long instanceId, long reasonId, String title, String content) throws ProhibitedComplaintActionExcrption, IncompleteComplaintDataExceptions;
    public void appointComplain(long userId, long complainId) throws IncorrectComplaintStateException;
    //public void updadeComplainResponse(long complainId, long userId, String response) throws IncorrectComplaintStateException;
    public void completeComplaint(long userId, long complainId, String responce) throws IncorrectComplaintStateException, IncompleteComplaintDataExceptions;
    public Collection<Complain> findByInstanceId(Long instanceId, long size, long offset);
    public void setResponsible(long responsibleId, long complaintId) throws IncorrectRoleException, IncorrectComplaintStateException;
}
