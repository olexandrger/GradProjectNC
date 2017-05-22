package com.grad.project.nc.service.notifications;

import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.User;
import sun.security.jca.GetInstance;

public interface EmailService {
    void sendRegistrationEmail(User user);
    void sendNewOrderEmail(ProductOrder order);
    void sendInstanceStatusChangedEmail(ProductInstance instance);
    void sendNewComplaintEmail(Complain complain);
    void sendComplaintUnderConsiderationChangedEmail(Complain complain);
    void sendComplaintCompleteEmail(Complain complain);
}
