package com.grad.project.nc.service.security;

import com.grad.project.nc.model.UserOLD;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(UserOLD userOLD);
}
