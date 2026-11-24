package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Location;
import com.warehouse.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public Result<List<Location>> list(@RequestParam(required = false) Boolean enabled) {
        return Result.success(locationService.list(enabled));
    }
}
