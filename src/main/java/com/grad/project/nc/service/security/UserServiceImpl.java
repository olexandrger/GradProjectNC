package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;
import com.grad.project.nc.model.UserOLD;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.UserDao;
import com.grad.project.nc.persistence.UserOLDDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("user was not found!"));
    }

    @Override
    public void createUser(User user) {
        user.setFirstName("test");
        user.setLastName("user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.add(user);
    }
}
