package com.grad.project.nc.controller.api.client;

import com.grad.project.nc.controller.api.dto.FrontendInstance;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.service.instances.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/instance")
public class InstancesController {

    private InstanceService instanceService;

    @Autowired
    public InstancesController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @RequestMapping(value = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendInstance getInstance(@PathVariable Long id) {
        return FrontendInstance.fromEntity(instanceService.getById(id));
    }
}
