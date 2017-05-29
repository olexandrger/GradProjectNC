package com.grad.project.nc.service.security;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.RoleDao;
import com.grad.project.nc.persistence.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RegistrationService registrationService;

    private String status;
    private String messageError;
    private final String SUCCESS = "success";
    private final String ERROR = "error";
    private final String INVALID_EMAIL = "Incorrect email address";
    private final String INCORRECT_PASSWORD = "Incorrect password. Minimum length - 8 symbols";
    private final String INCORRECT_PHONE = "Incorrect phone number";
    private final String FIRST_NAME_IS_EMPTY = "First name is empty";
    private final String LAST_NAME_IS_EMPTY = "Last name is empty";
    private final String EMAIL_IS_EMPTY = "Email is empty";
    private final String PASSWORD_IS_EMPTY = "Password is empty";
    private final String PHONE_IS_EMPTY = "Phone is empty";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("user was not found!"));
    }

    @Override
    public User createUser(String fistName, String lastName, String email, String password, String phone, List<String> roles) {
        User user = new User();

        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phone);

        user.setRoles(new LinkedList<>());

        roles.forEach(roleName -> {
            Role role = roleDao.findByName(roleName);
            if (role != null) {
                user.getRoles().add(role);
            } else {
                log.error("Cannot find role " + roleName);
            }
        });

        userDao.add(user);
        userDao.persistUserRoles(user);

        return user;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            return userDao.findByEmail(username).orElse(null);
        } else {
            return null;
        }

    }

    @Override
    public User findByEMail(String eMail) {
        return userDao.findByEmail(eMail).orElseGet(()->null);
    }

    /**
     * Because we store encoded user passwords,we cant se user password.If user password was not changed during editing
     * user info by admin, it value sets to null.That's indicates that we should not change password i DB.
     */
    @Override
    public Boolean update(User user) {

        if (user.getFirstName().isEmpty()) {
            status = ERROR;
            messageError = FIRST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getLastName().isEmpty()) {
            status = ERROR;
            messageError = LAST_NAME_IS_EMPTY;
            return false;
        }
        if (user.getEmail().isEmpty()) {
            status = ERROR;
            messageError = EMAIL_IS_EMPTY;
            return false;
        }
        if (user.getPhoneNumber().isEmpty()) {
            status = ERROR;
            messageError = PHONE_IS_EMPTY;
            return false;
        }
        if (!registrationService.isPhoneNumberValid(user.getPhoneNumber())) {
            status = ERROR;
            messageError = INCORRECT_PHONE;
            return false;
        }
        if (!registrationService.isEmailValid(user.getEmail())) {
            status = ERROR;
            messageError = INVALID_EMAIL;
            return false;
        }

        try {

            if (user.getPassword() == null) {
                user.getRoles();
                user.getDomains();
                userDao.updateWithoutPassword(user);
            } else {
                if (user.getPassword().isEmpty()) {
                    status = ERROR;
                    messageError = PASSWORD_IS_EMPTY;
                    return false;
                }
                if (!registrationService.isPasswordValid(user.getPassword())) {
                    status = ERROR;
                    messageError = INCORRECT_PASSWORD;
                    return false;
                }
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.getRoles();
                user.getDomains();
                userDao.update(user);
            }
        }
        catch (DataAccessException e){
            log.info("Error during user info update!");
            return false;
        }

        return true;
    }

    @Override
    public User updateGeneralInformation(User user) {
        userDao.updateGeneralInformation(user);
        return user;
    }

    @Override
    public User updatePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.updatePassword(user);
        return user;
    }

    @Override
    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    @Override
    public List<User> findUsersByRegionId(int id){
        return userDao.findUsersByRegionId(id);
    }

    @Override
    public List<User> findAllUsersSorted(String sort, Long size, Long offset){
        return userDao.findSorted(sortSql(sort), size, offset);
    }

    @Override
    public List<User> findAllUsersByPhoneSorted(String sort, Long size, Long offset, String phone){
        return userDao.findByPhoneSorted(sortSql(sort), size, offset, phone);
    }

    @Override
    public List<User> findUsersByRegionIdSorted(int id, String sort, Long size, Long offset){
        return userDao.findUsersByRegionIdSort(id, sortSql(sort), size, offset);
    }

    @Override
    public List<User> findUsersByRegionIdAndPhoneSorted(int id, String sort, Long size, Long offset, String phone){
        return userDao.findUsersByRegionIdAndPhoneSort(id, sortSql(sort), size, offset, phone);
    }

    public String sortSql(String sort){
        String sql = " order by ";
        if(sort.equals("email")){
            sql += "email";
        }else
        if (sort.equals("lastname")){
            sql += "last_name";
        }else {
            sql += "user_id desc";
        }
        return sql;
    }

    public String getStatus() {
        return status;
    }

    public String getMessageError() {
        return messageError;
    }

    @Override
    public Collection<User> findByRoleId(long roleId) {
       return userDao.findUsersByRole(roleId);
    }
}
