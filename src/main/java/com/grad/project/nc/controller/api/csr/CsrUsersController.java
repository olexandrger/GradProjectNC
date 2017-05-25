package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendUser;
import com.grad.project.nc.model.User;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DeniG on 10.05.2017.
 */

@RestController
@RequestMapping("/api/csr/users")
public class CsrUsersController {

    private UserService userService;

    @Autowired
    public CsrUsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/find/{mail}", method = RequestMethod.GET)
    FrontendUser findUserByEmail(@PathVariable String mail){
        return FrontendUser.fromEntity(userService.findByEMail(mail));
    }


/*    @RequestMapping(value = "/find/phone/{phone}", method = RequestMethod.GET)
    Collection<FrontendUser> findUsersByPhone(@PathVariable String phone){
        return userService.findUsersPhone(phone).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
    }*/

    @RequestMapping(value = "/find/all/size/{size}/offset/{offset}/region/{region}/sort/{sort}", method = RequestMethod.GET)
    Collection<FrontendUser> findUsersByRegion(@PathVariable Long size, @PathVariable Long offset, @PathVariable int region,@PathVariable String sort){
        if (region == 0){
            return userService.findAllUsersSorted(sort, size, offset).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
        }
        return userService.findUsersByRegionIdSorted(region, sort, size, offset).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    Collection<FrontendUser> getUsersByParams(@RequestParam(value = "size") Long size,
                                              @RequestParam(value = "offset") Long offset,
                                              @RequestParam(value = "regionId") int regionId,
                                              @RequestParam(value = "sort") String sort,
                                              @RequestParam(value = "phone", required = false) String phone
                                              ){
        Collection<FrontendUser> frontendUser;
        if (regionId == 0){
            if (phone.length() > 0){
                frontendUser = userService.findAllUsersByPhoneSorted(sort, size, offset, phone).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
            }else {
                frontendUser = userService.findAllUsersSorted(sort, size, offset).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
            }
        }
        else {
            if (phone.length() > 0){
                frontendUser = userService.findUsersByRegionIdAndPhoneSorted(regionId, sort, size, offset, phone).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
            }else {
                frontendUser = userService.findUsersByRegionIdSorted(regionId, sort, size, offset).stream().map(FrontendUser::fromEntity).collect(Collectors.toList());
            }
        }
        return frontendUser;
    }
}
