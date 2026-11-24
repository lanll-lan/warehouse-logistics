package com.warehouse.controller;

import com.warehouse.entity.Inventory;
import com.warehouse.service.InventoryService;
import com.warehouse.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public Result<List<Inventory>> list(@RequestParam(required = false) Long productId,
                                        @RequestParam(required = false) Long locationId) {
        return Result.success(inventoryService.list(productId, locationId));
    }
}
