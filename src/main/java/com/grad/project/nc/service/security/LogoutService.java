package com.grad.project.nc.service.security;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface LogoutService {
    public String getRedirectUrl(String url);
}
