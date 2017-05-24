package com.grad.project.nc.service.complain;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.exceptions.IncompleteComplaintDataExceptions;
import com.grad.project.nc.service.exceptions.IncorrectComplaintStateException;
import com.grad.project.nc.service.exceptions.IncorrectRoleException;
import com.grad.project.nc.service.exceptions.ProhibitedComplaintActionExcrption;
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
    private RoleDao roleDao;

    public static final long USER_ROLE_PMG = 4L;

    private static final long COMPLAIN_REASON_SERVICE_PROBLEMS = 17L;
    private static final long COMPLAIN_REASON_ORDER_PROBLEMS = 18L;
    private static final long COMPLAIN_REASON_TECHNICAL_PROBLEMS = 19L;

    private static final long COMPLAIN_STATUS_CREATED = 5L;
    private static final long COMPLAIN_STATUS_UNDER_CONSIDERATION = 6L;
    private static final long COMPLAIN_STATUS_CONSIDERATION_COMPLETED = 7L;
    //private static final long COMPLAIN_STATUS_REJECTED = 8L;

    @Autowired
    public ComplainServiceImpl(ComplainDao complainDao, UserDao userDao, ProductInstanceDao productInstanceDao, CategoryDao categoryDao, EmailService emailService, RoleDao roleDao) {
        this.complainDao = complainDao;
        this.userDao = userDao;
        this.productInstanceDao = productInstanceDao;
        this.categoryDao = categoryDao;
        this.emailService = emailService;
        this.roleDao = roleDao;
    }

    @Override
    public Collection<Complain> getAllComplains(long size, long offset) {
        return complainDao.findAll(size, offset);
    }

    @Override
    public Complain newComplain(long userId, long instanceId, long reasonId, String title, String content) throws ProhibitedComplaintActionExcrption, IncompleteComplaintDataExceptions {
        if (title == null || title.length() < 1) {
            throw new IncompleteComplaintDataExceptions("Subject can not be empty!");
        }
        Category created = categoryDao.find(COMPLAIN_STATUS_CREATED);
        User user = userDao.find(userId);
        ProductInstance productInstance = productInstanceDao.find(instanceId);

        Category reason = categoryDao.find(reasonId);

        if (productInstance != null && !userIsContainedInInstance(user, productInstance)) {
            throw new ProhibitedComplaintActionExcrption("You do not belong to the domain of this product");
        }
        Complain complain = new Complain();

        complain.setOpenDate(OffsetDateTime.now());
        complain.setStatus(created);
        complain.setUser(user);
        complain.setProductInstance(productInstance);
        complain.setComplainReason(reason);
        complain.setComplainTitle(title);
        complain.setContent(content);

        complain = complainDao.add(complain);
        emailService.sendNewComplaintEmail(complainDao.find(complain.getComplainId()));
        return complain;
    }

    @Override
    public void appointComplain(long userId, long complainId) throws IncorrectComplaintStateException {
        Complain complain = complainDao.find(complainId);
        User user = userDao.find(userId);
        if (complain.getStatus().getCategoryId().longValue() == COMPLAIN_STATUS_CONSIDERATION_COMPLETED) {
            throw new IncorrectComplaintStateException("You can not change a completed complaint!");
        }
        complain.setStatus(categoryDao.find(COMPLAIN_STATUS_UNDER_CONSIDERATION));
        complain.setResponsible(user);
        complain = complainDao.update(complain);
        emailService.sendComplaintUnderConsiderationChangedEmail(complainDao.find(complain.getComplainId()));
    }

//    @Override
//    public void updadeComplainResponse(long complainId, long userId, String response) throws IncorrectComplaintStateException {
//        Complain complain = complainDao.find(complainId);
//        if (complain.getStatus().getCategoryId().longValue() == COMPLAIN_STATUS_UNDER_CONSIDERATION
//                && complain.getResponsible().getUserId().longValue() != userId) {
//            throw new IncorrectComplaintStateException("You can not change a complaint, assigned to another responsible!");
//        }
//        if (complain.getStatus().getCategoryId().longValue() != COMPLAIN_STATUS_UNDER_CONSIDERATION
//                && complain.getStatus().getCategoryId().longValue() != COMPLAIN_STATUS_CREATED) {
//            throw new IncorrectComplaintStateException("You can not change a  complaint in status " + complain.getStatus().getCategoryName());
//        }
//        complain.setResponse(response);
//        complainDao.update(complain);
//    }

    @Override
    public void completeComplaint(long userId, long complainId, String responce) throws IncorrectComplaintStateException, IncompleteComplaintDataExceptions {
        Complain complain = complainDao.find(complainId);
        if (complain.getStatus().getCategoryId().longValue() != COMPLAIN_STATUS_UNDER_CONSIDERATION) {
            throw new IncorrectComplaintStateException("You can not end a problem with the status of " + complain.getStatus().getCategoryName());
        }
        if (complain.getResponsible() == null) {
            throw new IncorrectComplaintStateException("This complaint has no responsible!");
        }
        if (complain.getResponsible().getUserId().longValue() != userId) {
            throw new IncorrectComplaintStateException("You can not change a complaint, assigned to another responsible!");
        }
        if (responce == null || responce.length() < 1) {
            throw new IncompleteComplaintDataExceptions("You can not close a complaint without response!");
        }
        complain.setStatus(categoryDao.find(COMPLAIN_STATUS_CONSIDERATION_COMPLETED));
        complain.setCloseDate(OffsetDateTime.now());
        complain.setResponse(responce);
        complain = complainDao.update(complain);
        emailService.sendComplaintCompleteEmail(complainDao.find(complain.getComplainId()));
    }

    @Override
    public Collection<Complain> findByInstanceId(Long instanceId, long size, long offset) {
        return complainDao.findByInstanceId(instanceId, size, offset);
    }

    @Override
    public void setResponsible(long responsibleId, long complaintId) throws IncorrectRoleException, IncorrectComplaintStateException {
        Complain complain = complainDao.find(complaintId);
        User responsible = userDao.find(responsibleId);
        if (!isPmg(responsible)) {
            throw new IncorrectRoleException("User " + responsible.getFirstName()
                    + " " + responsible.getLastName() + " <" + responsible.getEmail() + ">"
                    + " is not a member of group "+roleDao.find(USER_ROLE_PMG).getRoleName());
        }
        if(complain.getStatus().getCategoryId().longValue()!=COMPLAIN_STATUS_UNDER_CONSIDERATION){
            throw new IncorrectComplaintStateException("Can not changecomplaint in state "+ complain.getStatus().getCategoryName());
        }
        complain.setResponsible(responsible);
        complainDao.update(complain);
    }


    private boolean userIsContainedInInstance(User user, ProductInstance productInstance) {
        for (User currentUser : productInstance.getDomain().getUsers()) {
            if (currentUser.getUserId().equals(user.getUserId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPmg(User user) {
        return  user.getRoles().stream().anyMatch(role -> {
            return role.getRoleId().longValue()==USER_ROLE_PMG;
        });

    }


}
