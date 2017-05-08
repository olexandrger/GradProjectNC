package com.grad.project.nc.controller.api;

import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.RegionDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/regions/")
public class RegionsController {
    private CrudDao<Region> regionsDao;

    @Autowired
    public RegionsController(RegionDao regionsDao) {
        this.regionsDao = regionsDao;
    }

    @RequestMapping("/all")
    public Collection<SimpleRegion> test() {
        return regionsDao.findAll().stream()
                .map((item) -> new SimpleRegion(item.getRegionId(), item.getRegionName()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    private static class SimpleRegion {
        private Long regionId;
        private String regionName;
    }
}
