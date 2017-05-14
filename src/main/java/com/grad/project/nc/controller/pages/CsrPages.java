package com.grad.project.nc.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CsrPages {
    @RequestMapping("/csr/orders")
    public String ordersPage() {
        return "/csr/orders";
    }

    @RequestMapping("/csr/reports")
    public String reportsPage() {
        return "/admin/report";
    }
}
