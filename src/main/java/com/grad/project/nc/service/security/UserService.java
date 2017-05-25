package com.grad.project.nc.service.security;

import com.grad.project.nc.model.User;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    User createUser(String fistName, String lastName, String email, String password, String phone, List<String> roles);

    User getCurrentUser();

    User findByEMail(String eMail);

    Boolean update(User user);

    User updateGeneralInformation(User user);

    User updatePassword(User user);

    public List<User> findAllUsers();

    public List<User> findUsersByRegionId(int id);

    public List<User> findAllUsersSorted(String sort, Long size, Long offset);

    public List<User> findAllUsersByPhoneSorted(String sort, Long size, Long offset, String phone);

    public List<User> findUsersByRegionIdAndPhoneSorted(int id, String sort, Long size, Long offset, String phone);

    public List<User> findUsersByRegionIdSorted(int id, String sort, Long size, Long offset);

    public String getStatus();

    public String getMessageError();

    public Collection<User> findByRoleId(long roleId);

}
