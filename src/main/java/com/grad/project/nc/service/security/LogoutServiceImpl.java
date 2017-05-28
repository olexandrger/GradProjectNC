package com.grad.project.nc.service.security;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogoutServiceImpl implements LogoutService {

    private static final String REDIRECT_URL = "/";
    private static final List<String> checkedUrls;

    static {
        checkedUrls = new ArrayList();
        checkedUrls.add("admin");
        checkedUrls.add("csr");
        checkedUrls.add("client");
        checkedUrls.add("pmg");
        checkedUrls.add("api/admin");
        checkedUrls.add("api/csr");
        checkedUrls.add("api/client");
        checkedUrls.add("api/pmg");
        checkedUrls.add("profile");
        checkedUrls.add("api/profile");
    }

    public String getRedirectUrl(String url) {
        if (checkedUrls
                .stream()
                .filter(url::startsWith)
                .findFirst().isPresent())
            return REDIRECT_URL;
        return url;
    }
}
