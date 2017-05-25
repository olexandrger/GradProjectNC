package com.grad.project.nc.controller.api.client;

import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.instances.InstanceService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/instance")
public class InstancesController {

    private InstanceService instanceService;
    private UserService userService;
    private static final long INSTANCE_STATUS_DEACTIVATED = 12;


    @Autowired
    public InstancesController(InstanceService instanceService, UserService userService) {
        this.instanceService = instanceService;
        this.userService = userService;
    }

    @RequestMapping(value = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendInstance getInstance(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CSR")) || instanceService.isInstanceOwnedBy(id, user.getUserId())) {
            return FrontendInstance.fromEntity(instanceService.getById(id));
        } else {
            throw new AccessDeniedException("You can not access this instance");
        }
    }

    @RequestMapping(value = "/find/byUser/", method = RequestMethod.GET)
    Collection<FrontendInstance> findByUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return instanceService.getByUserId(user.getUserId())
                .stream()
                .filter(instance->{ return instance.getStatus().getCategoryId().longValue() != INSTANCE_STATUS_DEACTIVATED;})
                .map(FrontendInstance::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
