package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.complain.ComplainService;
import com.grad.project.nc.service.instances.InstanceService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by DeniG on 11.05.2017.
 */
@RestController
@RequestMapping("/api/csr/instances")
public class CsrInstanceController {

    private static final long INSTANCE_STATUS_DEACTIVATED = 12;
    InstanceService instanceService;
    private UserService userService;
    private ComplainService complainService;

    @Autowired
    public CsrInstanceController(InstanceService instanceService,UserService userService,ComplainService complainService) {
        this.instanceService = instanceService;
        this.userService = userService;
        this.complainService = complainService;
    }

    @RequestMapping(value = "/find/bydomain/{id}", method = RequestMethod.GET)
    Collection<FrontendInstance> findByDomainID(@PathVariable Long id){
        return instanceService.getByDomainId(id)
                .stream()
                .filter(instance-> instance.getStatus().getCategoryId() != INSTANCE_STATUS_DEACTIVATED)
                .map(FrontendInstance::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }



    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    Collection<FrontendInstance> findAll(){
        return instanceService.getAll()
                .stream()
                .filter(instance->{ return instance.getStatus().getCategoryId().longValue() != INSTANCE_STATUS_DEACTIVATED;})
                .map(FrontendInstance::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequestMapping(value = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendInstance getInstance(@PathVariable Long id) {


            return FrontendInstance.fromEntity(instanceService.getById(id));

    }

    @RequestMapping(value = "complains/get/byInstance/{instanceId}/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getOrdersByInstance(@PathVariable Long instanceId, @PathVariable Long size, @PathVariable Long offset) {
        return complainService.findByInstanceId(instanceId, size, offset)
                .stream()
                .map(FrontendComplain::fromEntity)
                .collect(Collectors.toList());
    }
}
