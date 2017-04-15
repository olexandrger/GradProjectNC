package com.grad.project.nc.persistence;

import com.grad.project.nc.model.User;

import java.util.Arrays;
import java.util.List;

public class DatabaseStub {

    public static List<User> getUsers() {
        return Arrays.asList(
                new User("Vasya", "Pupkin"),
                new User("Ivan", "Ivanov")
        );
    }
}
