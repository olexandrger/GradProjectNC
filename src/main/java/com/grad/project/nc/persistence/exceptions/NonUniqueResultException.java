package com.grad.project.nc.persistence.exceptions;

public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException() {
    }

    public NonUniqueResultException(String message) {
        super(message);
    }
}
