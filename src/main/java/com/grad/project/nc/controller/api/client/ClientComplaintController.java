package com.grad.project.nc.controller.api.client;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.controller.api.dto.FrontendOrder;
import com.grad.project.nc.service.complain.ComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 20.05.2017.
 */

@RestController
@RequestMapping("/api/client/complaints")
public class ClientComplaintController {

    ComplainService complainService;

    @Autowired
    public ClientComplaintController(ComplainService complainService) {
        this.complainService = complainService;
    }

    @RequestMapping(value = "/get/byInstance/{instanceId}/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getOrdersByInstance(@PathVariable Long instanceId, @PathVariable Long size, @PathVariable Long offset) {
        return complainService.findByInstanceId(instanceId, size, offset)
                .stream()
                .map(FrontendComplain::fromEntity)
                .collect(Collectors.toList());
    }

}
