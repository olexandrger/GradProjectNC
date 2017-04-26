package com.grad.project.nc.model;

import java.util.ArrayList;

/**
 * Created by Alex on 4/24/2017.
 */
public class User {
    private Long user_id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ArrayList<Role> authorities;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
