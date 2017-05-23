package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.service.complain.ComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 23.05.2017.
 */

@RestController
@RequestMapping("api/admin/complaint")
public class AdminComplaintController {
    private ComplainService complainService;

    @Autowired
    public AdminComplaintController(ComplainService complainService) {
        this.complainService = complainService;
    }

    @RequestMapping(value = "/get/all/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getComplains(@PathVariable Long size, @PathVariable Long offset){
        return complainService.getAllComplains(size,offset).stream()
                .map(FrontendComplain :: fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}


