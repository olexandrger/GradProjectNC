package com.grad.project.nc.controller.api.pmg;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.service.complain.ComplainService;
import com.grad.project.nc.service.exceptions.IncompleteComplaintDataExceptions;
import com.grad.project.nc.service.exceptions.IncorrectComplaintStateException;
import com.grad.project.nc.service.exceptions.InsufficientRightsException;
import com.grad.project.nc.service.exceptions.ProhibitedComplaintActionExcrption;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 16.05.2017.
 */
@RestController
@RequestMapping("api/pmg/complaint")
public class PmgComplainController {

    private ComplainService complainService;
    private UserService userService;

    @Autowired
    public PmgComplainController(ComplainService complainService, UserService userService) {
        this.complainService = complainService;
        this.userService = userService;
    }

    @RequestMapping(value = "/get/all/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getComplains(@PathVariable Long size, @PathVariable Long offset){
        return complainService.getAllComplains(size,offset).stream()
                .map(FrontendComplain :: fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public Map<String, Object> newComplain(@RequestBody Map<String, String> params){
        Map<String, Object> result = new HashMap<>();
        try {
            long userId = Long.parseLong(params.get("userId"));
            long instanceId = Long.parseLong(params.get("instanceId"));
            long reasonId = Long.parseLong(params.get("reasonId"));
            String title = params.get("title");
            String content = params.get("content");
            Complain complain = complainService.newComplain(userId,instanceId,reasonId,title,content);
            result.put("status", "success");
            result.put("message", "Complain created");
            result.put("id", complain.getComplainId());
        } catch (NumberFormatException | ProhibitedComplaintActionExcrption | IncompleteComplaintDataExceptions ex){
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", ex.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/take/byId/", method = RequestMethod.POST)
    public Map<String, Object> takeComplain(@RequestBody Map<String, String> params){
        Map<String, Object> result = new HashMap<>();

        long userId = userService.getCurrentUser().getUserId();
        long complainId = Long.parseLong(params.get("complaintId"));
        try {
            complainService.appointComplain(userId, complainId);
            result.put("status", "success");
            result.put("message", "The complaint was successfully assigned!");
        } catch (IncorrectComplaintStateException ex){
            result.put("status", "fail");
            result.put("message", ex.getMessage());

        }
        return result;
    }

    @RequestMapping (value = "/complete/byid/", method = RequestMethod.POST)
    public Map<String, Object> completeComplain(@RequestBody Map<String, String> params){
        Map<String, Object> result = new HashMap<>();
        long userId =userService.getCurrentUser().getUserId();
        long complainId =Long.parseLong(params.get("complaintId"));
        String response = params.get("response");
        try {
            complainService.completeComplaint(userId, complainId, response);
            result.put("status", "success");
            result.put("message", "Response successfully complete");
        } catch (IncorrectComplaintStateException | IncompleteComplaintDataExceptions ex){
            result.put("status", "fail");
            result.put("message", ex.getMessage());
        }
        return result;
    }
}
