package com.grad.project.nc.model;

import java.util.ArrayList;


public class Account {
    private Long account_id;
    private String username;
    private String password;
    private ArrayList<Role> authorities;

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<Role> authorities) {
        this.authorities = authorities;
    }
}

