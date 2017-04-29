package com.grad.project.nc.controller.api.data;

import lombok.Data;

@Data
public class RegistrationResponseHolder {

    private String status;
    private String messageError;
    private String url;

}
