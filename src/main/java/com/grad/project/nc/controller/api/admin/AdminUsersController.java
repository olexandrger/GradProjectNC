package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.data.RegistrationResponseHolder;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.controller.api.dto.FrontendUser;
import com.grad.project.nc.model.*;
import com.grad.project.nc.model.proxy.UserProxy;
import com.grad.project.nc.persistence.RoleDao;
import com.grad.project.nc.persistence.UserDao;
import com.grad.project.nc.service.exceptions.IncorrectUserDataException;
import com.grad.project.nc.service.locations.LocationService;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.RegistrationService;
import com.grad.project.nc.service.security.UserService;
import com.grad.project.nc.service.security.UserServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Alex on 5/8/2017.
 */

@RestController
@RequestMapping("/api/admin/users")
@Slf4j
public class AdminUsersController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private LocationService locationService;

    private static final long USER_ROLE_PMG =4;

    private static final long USER_ROLE_CSR =3;

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public RegistrationResponseHolder add(@RequestBody FrontUser frontUser) {
        User user = mapFrontUserToUser(frontUser);
        RegistrationResponseHolder registrationResponse = new RegistrationResponseHolder();
        log.info("Registering " + frontUser.getRoles().toString());

        try {
            registrationService.validation(user);
            registrationService.register(user);
            emailService.sendRegistrationEmail(user);
            registrationResponse.setMessage("User registered successfully");
        } catch (IncorrectUserDataException e) {
            registrationResponse.setMessage(e.getMessage());
        }
        registrationResponse.setStatus(registrationService.getStatus());
        return registrationResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public RegistrationResponseHolder update(@RequestBody FrontUser frontUser) {
        User user = mapFrontUserToUser(frontUser);
        RegistrationResponseHolder updateResponse = new RegistrationResponseHolder();
        log.info("Updating by admin " + frontUser.getFirstName());

        if (!userService.update(user)){
            updateResponse.setMessage(userService.getMessageError());
            updateResponse.setStatus(userService.getStatus());
        }
        else {
            updateResponse.setMessage("User updated ");
            updateResponse.setStatus("success");

        }

        return updateResponse;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public FrontUser get(@RequestParam("name") String name) {
        log.info( "Found:" + name  );
        FrontUser frontUser = mapUserToFrontUser(userService.findByEMail(name));

        return frontUser;
    }

    @RequestMapping(value = "/getAddressForUserDomain/", method = RequestMethod.GET)
    @ResponseBody
    public String getAddress(@RequestParam("id") String id) {

        locationService.doRequestForJSONByGooglePlaceId(id);
        return locationService.getCity() +", " + locationService.getStreet() + " " + locationService.getBuildingNumber();
    }

    @RequestMapping(value = "/userRoles", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, String> userRoles() {
        Map<Long, String> result = new HashMap<>();
        roleDao.findAll().forEach(value->result.put(value.getRoleId(),value.getRoleName()));
        return result;
    }

    @RequestMapping(value = "/find/csr", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendUser> findCsrs() {
        return getUsersByrole(USER_ROLE_CSR);
    }

    @RequestMapping(value = "/find/pmg", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendUser> findPmgs() {
        return getUsersByrole(USER_ROLE_PMG);
    }


    private Collection<FrontendUser> getUsersByrole(long roleId){
        return userService.findByRoleId(roleId).stream().map(FrontendUser::fromEntity).collect(Collectors.toCollection(ArrayList::new));
    }

    private User mapFrontUserToUser(FrontUser frontUser){
        User user = new User();

        user.setUserId(frontUser.getUserId());
        user.setFirstName(frontUser.getFirstName());
        user.setLastName(frontUser.getLastName());
        user.setEmail(frontUser.getEmail());
        user.setPassword(frontUser.getPassword());
        user.setPhoneNumber(frontUser.getPhoneNumber());
        user.setRoles(frontUser.getRoles());
        user.setDomains(frontUser.getDomains().stream()
                .map(frontendDomain -> Domain.builder().domainId(frontendDomain.getDomainId()).build())
                .collect(Collectors.toList()));

        return user;
    }
    private FrontUser mapUserToFrontUser(User user){

         return FrontUser.builder().userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .domains(user.getDomains().stream()
                        .map(FrontendDomain::fromEntity)
                        .collect(Collectors.toList()))
                .build();

    }

    @Data
    @Builder
    private static   class FrontUser{
        private Long userId;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String address;
        private Number aptNumber;

        private List<Role> roles;
        private List<FrontendDomain> domains;


    }
}
