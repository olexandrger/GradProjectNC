package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendAddress;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/12/2017.
 */
@RestController
@RequestMapping("/api/admin/domains")
@Slf4j
public class AdminDomainController {

    @RequestMapping(path = "/getDomainsByAddress", method = RequestMethod.GET)
    @ResponseBody
    public List<FrontendDomain> getDomainsByAddress(@RequestParam("address") String address, @RequestParam("aptNum") String aptNum) {

        //TODO rework after domains will be finished
        List<FrontendDomain> list = new ArrayList<>();
        list.add(FrontendDomain.builder().domainId((long) -3).domainName("Mock domain for: "+ address + aptNum). build());


        return list;
    }

    @RequestMapping(value = "/addDomain", method = RequestMethod.GET)
    @ResponseBody
    public FrontendDomain addDomain(@RequestParam("address") String address,@RequestParam("aptNum") String aptNum
            ,@RequestParam("type") String type,@RequestParam("name") String name) {

        FrontendDomain frontendDomain = FrontendDomain.builder().domainId(Long.valueOf(("-3")))
                .domainName( "Mock domain for"+name).address(FrontendAddress.builder().apartment(aptNum).build()).build();

        //TODO domainService
        //FrontendDomain frontendDomain = domainService.addDomain(address,aptNum,type,name);


        return frontendDomain;
    }
}
