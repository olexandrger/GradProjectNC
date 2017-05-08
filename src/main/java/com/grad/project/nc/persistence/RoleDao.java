package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Role;

import java.util.List;

public interface RoleDao extends CrudDao<Role> {
    Role findByName(String roleName);

    List<Role> findUserRolesById(Long userId);
}
