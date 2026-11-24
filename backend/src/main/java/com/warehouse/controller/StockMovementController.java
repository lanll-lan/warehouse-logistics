package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.StockMovement;
import com.warehouse.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    public Result<List<StockMovement>> list(@RequestParam(required = false) Long inventoryId,
                                            @RequestParam(required = false) String movementType) {
        return Result.success(stockMovementService.list(inventoryId, movementType));
    }
}
