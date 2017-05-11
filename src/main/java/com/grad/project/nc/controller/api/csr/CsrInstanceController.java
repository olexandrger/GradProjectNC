package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendInstance;
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
 * Created by DeniG on 11.05.2017.
 */
@RestController
@RequestMapping("/api/csr/instances")
public class CsrInstanceController {

    InstanceService instanceService;

    @Autowired
    public CsrInstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    Collection<FrontendInstance> findByID(@PathVariable Long id){
        return instanceService.getByDomainId(id)
                .stream()
                .map(FrontendInstance::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
