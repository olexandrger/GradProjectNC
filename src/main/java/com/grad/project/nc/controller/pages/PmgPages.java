package com.grad.project.nc.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by DeniG on 16.05.2017.
 */
@Controller
public class PmgPages {
    @RequestMapping("/pmg/complains")
    public  String complainspage(){
        return ("/pmg/complains");
    }
}
