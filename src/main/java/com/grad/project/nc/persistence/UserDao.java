package com.grad.project.nc.persistence;

import com.grad.project.nc.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudDao<User> {
    List<User> findByDomainId(Long domainId);

    void deleteUserRoles(Long userId);
    void deleteUserDomains(Long userId);

    void persistUserRoles(User user);
    @Transactional
    void persistUserDomains(User user);

    Optional<User> findByEmail(String email);

    User findUserByProductOrderId(Long productOrderId);

    User findResponsibleByProductOrderId(Long productOrderId);

    User findUserByComplainId(Long complainId);

    List<User> findUsersByRegionId(int id);

    public List<User> findByPhoneSorted(String sort, Long size, Long offset, String phone);

    public List<User> findUsersByRegionIdAndPhoneSort(int id, String sort, Long size, Long offset, String phone);

    User findResponsibleByComplainId(Long complainId);

    User updateWithoutPassword(User user);

    User updateGeneralInformation(User user);

    User updatePassword(User user);

    void addUserRole(Long userId, Long roleId);

    void deleteUserRole(Long userId, Long roleId);

    void addUserDomain(Long userId, Long roleId);

    void deleteUserDomain(Long userId, Long roleId);

    public List<User> findSorted(String sort, Long size, Long offset);

    public List<User> findUsersByRegionIdSort(int id, String sort, Long size, Long offset);

    public Collection<User> findUsersByRole(long roleId);
    }
