package com.grad.project.nc.service.exceptions;

/**
 * Created by DeniG on 18.05.2017.
 */
public class IncorrectItemStateException extends RuntimeException {
    public IncorrectItemStateException() {
        super();
    }

    public IncorrectItemStateException(String s) {
        super(s);
    }
}
