package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Data
public class Role implements GrantedAuthority {
    Long roleId;
    String roleName;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
