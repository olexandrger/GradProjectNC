package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendUser;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
