package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(User user);
}
