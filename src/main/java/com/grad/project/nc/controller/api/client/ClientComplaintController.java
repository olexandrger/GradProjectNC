package com.grad.project.nc.controller.api.client;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.service.complain.ComplainService;
import com.grad.project.nc.service.exceptions.IncompleteComplaintDataExceptions;
import com.grad.project.nc.service.exceptions.InsufficientRightsException;
import com.grad.project.nc.service.exceptions.ProhibitedComplaintActionExcrption;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 20.05.2017.
 */

@RestController
@RequestMapping("/api/client/complaints")
public class ClientComplaintController {

    ComplainService complainService;
    UserService userService;

    @Autowired
    public ClientComplaintController(ComplainService complainService, UserService userService) {
        this.complainService = complainService;
        this.userService = userService;
    }

    @RequestMapping(value = "/get/byInstance/{instanceId}/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getOrdersByInstance(@PathVariable Long instanceId, @PathVariable Long size, @PathVariable Long offset) {
        return complainService.findByInstanceId(instanceId, size, offset)
                .stream()
                .map(FrontendComplain::fromEntity)
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public Map<String, Object> newComplain(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long userId = getCurrentUserId();
            long instanceId = Long.parseLong(params.get("instanceId"));
            long reasonId = Long.parseLong(params.get("reasonId"));
            String title = params.get("title");
            String content = params.get("content");
            Complain complain = complainService.newComplain(userId, instanceId, reasonId, title, content);
            result.put("status", "success");
            result.put("message", "Complain created");
            result.put("id", complain.getComplainId());
        } catch (NumberFormatException | ProhibitedComplaintActionExcrption | IncompleteComplaintDataExceptions e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEMail(auth.getName()).getUserId();
    }

}
