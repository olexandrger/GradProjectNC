package com.grad.project.nc.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class CsrPages {
    @RequestMapping("/csr/orders")
    public String ordersPage() {
        return "/csr/orders";
    }

    @RequestMapping("/csr/orders/*")
    public String orderPage() {
        return "/csr/order";
    }

    @RequestMapping("/csr/reports")
    public String reportsPage() {
        return "/admin/report";
    }

}
