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

   // @Autowired
    //public UserServiceImpl(/*UserDao userDao, RoleDao roleDao,*/ BCryptPasswordEncoder bCryptPasswordEncoder) {
        //this.userDao = userDao;
        //this.roleDao = roleDao;
      //  this.bCryptPasswordEncoder = bCryptPasswordEncoder;
   // }

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

        try {

            if (user.getPassword() == null) {
                user.getRoles();
                user.getDomains();
                userDao.updateWithoutPassword(user);
            } else {
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
}
