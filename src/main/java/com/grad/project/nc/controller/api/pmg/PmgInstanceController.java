package com.grad.project.nc.controller.api.pmg;

import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.service.instances.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 17.05.2017.
 */
@RestController
@RequestMapping("/api/pmg/instances")
public class PmgInstanceController {

    private static final long INSTANCE_STATUS_DEACTIVATED = 12;
    InstanceService instanceService;

    @Autowired
    public PmgInstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }
    @RequestMapping(value = "/find/bydomain/{id}", method = RequestMethod.GET)
    Collection<FrontendInstance> findByDomainID(@PathVariable Long id){
        return instanceService.getByDomainId(id)
                .stream()
                .filter(instance->{ return instance.getStatus().getCategoryId().longValue()!=INSTANCE_STATUS_DEACTIVATED;})
                .map(FrontendInstance::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
