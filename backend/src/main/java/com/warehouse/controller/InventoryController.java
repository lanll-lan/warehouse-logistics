package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Inventory;
import com.warehouse.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public Result<List<Inventory>> list() {
        return Result.success(inventoryService.list());
    }

    @GetMapping("/{id}")
    public Result<Inventory> getById(@PathVariable Long id) {
        return Result.success(inventoryService.getById(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Inventory inventory) {
        inventoryService.save(inventory);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return Result.success();
    }
}
