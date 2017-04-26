package com.grad.project.nc.model;

import org.springframework.security.core.GrantedAuthority;

public enum RoleOld implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
