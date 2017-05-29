package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.client.InstancesController;
import com.grad.project.nc.controller.api.dto.FrontendComplain;
import com.grad.project.nc.controller.api.dto.catalog.FrontendCatalogDiscount;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstanceAddress;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.complain.ComplainService;
import com.grad.project.nc.service.discount.DiscountService;
import com.grad.project.nc.service.instances.InstanceService;
import com.grad.project.nc.service.locations.LocationService;
import com.grad.project.nc.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.stream.Collectors;


/**
 * Created by DeniG on 11.05.2017.
 */
@RestController
@Slf4j
@RequestMapping("/api/csr/instances")
public class CsrInstanceController  {

    private InstanceService instanceService;
    private ComplainService complainService;

    @Autowired
    public CsrInstanceController(InstanceService instanceService,ComplainService complainService) {
        this.instanceService = instanceService;
        this.complainService = complainService;

    }

    @RequestMapping(value = "/find/bydomain/{id}", method = RequestMethod.GET)
    Collection<FrontendInstance> findByDomainID(@PathVariable Long id){
        return instanceService.getByDomainId(id)
                .stream()
                //.filter(instance-> instance.getStatus().getCategoryId() != INSTANCE_STATUS_DEACTIVATED)
                .map(instance -> {
                    FrontendInstance frontendInstance =FrontendInstance.fromEntity(instance);
                    frontendInstance = instanceService.setAddressAndDiscount(frontendInstance);
                    return frontendInstance;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }



    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    Collection<FrontendInstance> findAll(){
        return instanceService.getAll()
                .stream()
                //.filter(instance->{ return instance.getStatus().getCategoryId().longValue() != INSTANCE_STATUS_DEACTIVATED;})
                .map(instance -> {
                    FrontendInstance frontendInstance =FrontendInstance.fromEntity(instance);
                    //frontendInstance = instanceService.setAddressAndDiscount(frontendInstance);
                    return frontendInstance;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequestMapping(value = "/all/size/{size}/offset/{offset}", method = RequestMethod.GET)
    Collection<FrontendInstance> findAllPage(@PathVariable Long size, @PathVariable Long offset){
        return instanceService.getAllPage(size, offset)
                .stream()
                .map(instance -> {
                    FrontendInstance frontendInstance =FrontendInstance.fromEntity(instance);
                    //frontendInstance = instanceService.setAddressAndDiscount(frontendInstance);
                    return frontendInstance;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @RequestMapping(value = "/all/size/{size}/offset/{offset}/status/{statusId}", method = RequestMethod.GET)
    Collection<FrontendInstance> findAllPageByStatus(@PathVariable Long size, @PathVariable Long offset,@PathVariable Long statusId){
        return instanceService.getAllByStatusId(size, offset,statusId)
                .stream()
                .map(instance -> {
                    FrontendInstance frontendInstance =FrontendInstance.fromEntity(instance);
                    //frontendInstance = instanceService.setAddressAndDiscount(frontendInstance);
                    return frontendInstance;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequestMapping(value = "/get/byId/{id}", method = RequestMethod.GET)
    public FrontendInstance getInstance(@PathVariable Long id) {

        FrontendInstance frontendInstance = FrontendInstance.fromEntity(instanceService.getById(id));

        Long start = System.currentTimeMillis();
        frontendInstance = instanceService.setAddressAndDiscount(frontendInstance);
        Long end = System.currentTimeMillis() - start;

        log.info("Time: {}", end);
        return frontendInstance;



    }

    @RequestMapping(value = "complains/get/byInstance/{instanceId}/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendComplain> getOrdersByInstance(@PathVariable Long instanceId, @PathVariable Long size, @PathVariable Long offset) {
        return complainService.findByInstanceId(instanceId, size, offset)
                .stream()
                .map(FrontendComplain::fromEntity)
                .collect(Collectors.toList());
    }


}
