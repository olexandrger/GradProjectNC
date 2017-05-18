package com.grad.project.nc.service.complain;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ComplainDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.UserDao;
import com.grad.project.nc.service.exceptions.IncorrectItemStateException;
import com.grad.project.nc.service.notifications.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;

/**
 * Created by DeniG on 16.05.2017.
 */
@Service
public class ComplainServiceImpl implements ComplainService {

    private ComplainDao complainDao;
    private UserDao userDao;
    private ProductInstanceDao productInstanceDao;
    private CategoryDao categoryDao;
    private EmailService emailService;

    private static final long COMPLAIN_REASON_SERVICE_PROBLEMS = 17L;
    private static final long COMPLAIN_REASON_ORDER_PROBLEMS = 18L;
    private static final long COMPLAIN_REASON_TECHNICAL_PROBLEMS = 19L;

    private static final long COMPLAIN_STATUS_CREATED = 5L;
    private static final long COMPLAIN_STATUS_UNDER_CONSIDERATION = 6L;
    private static final long COMPLAIN_STATUS_CONSIDERATION_COMPLETED = 7L;
    //private static final long COMPLAIN_STATUS_REJECTED = 8L;

    @Autowired
    public ComplainServiceImpl(ComplainDao complainDao, UserDao userDao, ProductInstanceDao productInstanceDao, CategoryDao categoryDao, EmailService emailService) {
        this.complainDao = complainDao;
        this.userDao = userDao;
        this.productInstanceDao = productInstanceDao;
        this.categoryDao = categoryDao;
        this.emailService = emailService;
    }

    @Override
    public Collection<Complain> getAllComplains(long size, long offset) {
        return complainDao.findAll(size, offset);
    }

    @Override
    public Complain newComplain(long userId, long instanceId, long reasonId, String title, String content) {
        Category created = categoryDao.find(COMPLAIN_STATUS_CREATED);
        User user = userDao.find(userId);
        ProductInstance productInstance = productInstanceDao.find(instanceId);

        Category reason = categoryDao.find(reasonId);

        Complain complain = new Complain();

        complain.setOpenDate(OffsetDateTime.now());
        complain.setStatus(created);
        complain.setUser(user);
        complain.setProductInstance(productInstance);
        complain.setComplainReason(reason);
        complain.setComplainTitle(title);
        complain.setContent(content);

        complain = complainDao.add(complain);
        emailService.sendNewComplainEmail(complain);
        return complain;
    }

    @Override
    public void appointComplain(long userId, long complainId) {
        Complain complain = complainDao.find(complainId);
        User user = userDao.find(userId);
        if(complain.getStatus().getCategoryId()==COMPLAIN_STATUS_CONSIDERATION_COMPLETED){
            throw new IncorrectItemStateException("You can not change a completed complaint!");
        }
        complain.setStatus(categoryDao.find(COMPLAIN_STATUS_UNDER_CONSIDERATION));
        complain.setResponsible(user);
        complainDao.update(complain);
    }

    @Override
    public void updadeComplainResponse(long complainId, long userId, String response) {
        Complain complain = complainDao.find(complainId);
        if(complain.getStatus().getCategoryId() == COMPLAIN_STATUS_UNDER_CONSIDERATION && complain.getResponsible().getUserId()!=userId){
            throw new IncorrectItemStateException("You can not change a complaint, assigned to another responsible!");
        }
        complain.setResponse(response);
        complainDao.update(complain);
    }

    @Override
    public void completeComplaint(long userId, long complainId) {
        Complain complain = complainDao.find(complainId);
        if(complain.getStatus().getCategoryId() != COMPLAIN_STATUS_UNDER_CONSIDERATION){
            throw new IncorrectItemStateException("You can not end a problem with the status of "+complain.getStatus().getCategoryName());
        }
        if(complain.getResponsible()==null){
            throw new IncorrectItemStateException("This complaint has no responsible!");
        }
        if(complain.getResponsible().getUserId()!=userId){
            throw new IncorrectItemStateException("You can not change a complaint, assigned to another responsible!");
        }
        complain.setStatus(categoryDao.find(COMPLAIN_STATUS_CONSIDERATION_COMPLETED));
        complain.setCloseDate(OffsetDateTime.now());
        complainDao.update(complain);
    }


}
