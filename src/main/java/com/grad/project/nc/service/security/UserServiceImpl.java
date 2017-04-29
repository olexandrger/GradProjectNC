package com.grad.project.nc.service.security;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import com.grad.project.nc.model.UserOLD;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.RoleDao;
import com.grad.project.nc.persistence.UserDao;
import com.grad.project.nc.persistence.UserOLDDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService{


    private UserDao userDao;
    private RoleDao roleDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
            Role role = roleDao.getRoleByName(roleName);
            if (role != null) {
                user.getRoles().add(role);
            } else {
                log.error("Cannot find role " + roleName);
            }
        });

        userDao.add(user);

        return user;
    }
}
