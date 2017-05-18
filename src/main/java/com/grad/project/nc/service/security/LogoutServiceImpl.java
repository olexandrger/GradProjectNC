package com.grad.project.nc.service.security;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogoutServiceImpl implements LogoutService {

    private final String REDIRECT_URL = "/";
    private final String EMPTY_STRING= "";

    private List<String> checkedUrls = new ArrayList();

    public LogoutServiceImpl(){
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

    @Override
    public Map getRedirectUrl(String url) {
        Map map = new HashMap<String, String>();
        for (String page : checkedUrls) {
            if (url.startsWith(page)) {
                map.put("redirect", REDIRECT_URL);
                return map;
            }
        }
        map.put("redirect", EMPTY_STRING);
        return map;
    }
}
