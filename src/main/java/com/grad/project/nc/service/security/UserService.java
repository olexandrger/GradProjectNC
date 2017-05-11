package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User createUser(String fistName, String lastName, String email, String password, String phone, List<String> roles);
    User findByEMail(String eMail);
}
