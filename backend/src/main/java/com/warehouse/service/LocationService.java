package com.warehouse.service;

import com.warehouse.entity.Location;
import com.warehouse.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationMapper locationMapper;

    public List<Location> list(Boolean enabled) {
        return locationMapper.findAll(enabled);
    }

    public Location getById(Long id) {
        Location location = locationMapper.findById(id);
        if (location == null) {
            throw new RuntimeException("库位不存在");
        }
        return location;
    }
}
