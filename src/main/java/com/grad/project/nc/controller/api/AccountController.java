package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.RoleDao;
import com.grad.project.nc.persistence.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AccountController {

    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public AccountController(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @RequestMapping(value = "/api/user/account", method = RequestMethod.GET)
    public Map<String, Object> getName() {
        Map<String, Object> result = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userDao.findByEmail(auth.getName());

        if (auth.isAuthenticated() && userOptional.isPresent()) {
            User user = userOptional.get();
            result.put("authenticated", "true");
            result.put("name", user.getFirstName() + " " + user.getLastName());
            result.put("userId", user.getUserId());
            List<Link> profileLinks = new LinkedList<>();

            profileLinks.add(new Link("Edit profile", "/profile/edit"));

            if (user.getRoles().contains(roleDao.findByName("ROLE_CLIENT"))) {
                profileLinks.add(new Link("Domains", "/client/domains"));

            }

            if (user.getRoles().contains(roleDao.findByName("ROLE_ADMIN"))) {
                profileLinks.add(new Link("Edit product types", "/admin/productTypes"));
                profileLinks.add(new Link("Edit products", "/admin/products"));
                profileLinks.add(new Link("Users", "/admin/users"));
                profileLinks.add(new Link("Discounts", "/admin/discounts"));

            }

            if (user.getRoles().contains(roleDao.findByName("ROLE_CSR"))) {
                profileLinks.add(new Link("Orders", "/csr/orders"));
                profileLinks.add(new Link("Reports", "/csr/reports"));
               // profileLinks.add(new Link("Complains", "/csr/complains"));
            }

            if (user.getRoles().contains(roleDao.findByName("ROLE_PMG"))) {
                profileLinks.add(new Link("Complains", "/pmg/complains"));
            }
            result.put("profileLinks", profileLinks);
        } else {
            result.put("authenticated", "false");
        }

        return result;
    }

    @Data
    private static class Link {
        Link(String name, String reference) {
            this.name = name;
            this.reference = reference;
        }

        private String name;
        private String reference;
    }
}
