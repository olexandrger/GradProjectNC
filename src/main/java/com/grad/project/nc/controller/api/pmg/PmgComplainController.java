package com.grad.project.nc.controller.api.pmg;

import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.service.complain.ComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 16.05.2017.
 */
@RestController
@RequestMapping("api/pmg/complaint")
public class PmgComplainController {

    private ComplainService complainService;

    @Autowired
    public PmgComplainController(ComplainService complainService) {
        this.complainService = complainService;
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
            result.put("message", "Order created");
            result.put("id", complain.getComplainId());
        } catch (NumberFormatException ex){
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        }
        return result;
    }
}
