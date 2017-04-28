package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<Role> roles;
    private List<Domain> domains;
    private List<ProductOrder> orders;
    private List<Complain> complains;
}
